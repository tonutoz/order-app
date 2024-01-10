package io.whatap.assignment.order;

import io.whatap.assignment.cmm.aop.MethodLogWriter;
import io.whatap.assignment.cmm.exception.RestApiException;
import io.whatap.assignment.order.domain.Order;
import io.whatap.assignment.order.domain.OrderProducts;
import io.whatap.assignment.order.domain.OrderStatus;
import io.whatap.assignment.order.domain.OrderSteps;
import io.whatap.assignment.order.domain.OrderStep;
import io.whatap.assignment.order.dto.OrderModifyRequest;
import io.whatap.assignment.order.dto.OrderRequest;
import io.whatap.assignment.order.dto.OrderResponse;
import io.whatap.assignment.order.dto.ProductResponse;
import io.whatap.assignment.order.exception.OrderError;
import io.whatap.assignment.order.product.dto.ProductOrderRequest;
import io.whatap.assignment.order.product.dto.ProductOrderResponse;
import io.whatap.assignment.order.product.ProductOrderService;
import io.whatap.assignment.order.repository.OrderProductsRepository;
import io.whatap.assignment.order.repository.OrderRepository;
import io.whatap.assignment.order.repository.OrderStepRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;

  private final OrderProductsRepository orderProductsRepository;

  private final OrderStepRepository orderStepRepository;

  private final ProductOrderService productApi;

  /**
   * 주문 조회 서비스
   *
   * @param id
   * @return
   */
  @MethodLogWriter
  public OrderResponse getOrder(final Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RestApiException(OrderError.ORDER_NOT_FOUND));
    log.debug("Order Product Size {}", order.getOrderProducts().size());
    log.debug("Order Status Size {}", order.getOrderSteps().size());

    return OrderResponse.from(order);
  }

  /**
   * 주문 조회 리스트 (요청자 아이디별)
   *
   * @param requesterId
   * @return
   */
  @MethodLogWriter
  public List<OrderResponse> getOrderList(final String requesterId) {

    List<Order> list = orderRepository.findAllByRequesterId(requesterId);
    if(list.isEmpty()) {
      throw new RestApiException(OrderError.ORDER_NOT_FOUND);
    }
    return list.stream().map(OrderResponse::from).toList();

  }

  /**
   * 주문 생성 서비스
   *
   * @param reqeust
   * @return
   * @throws NullPointerException
   */
  @Transactional
  @MethodLogWriter
  public OrderResponse createOrder(final OrderRequest reqeust) throws NullPointerException {

    // 재고 요청 ..
    List<ProductOrderRequest> list =
        reqeust.getProductList().stream().map((p) -> ProductOrderRequest.builder()
            .productId(p.getProductId())
            .reqQuantity(p.getQuantity()).build()
        ).toList();

    List<ProductOrderResponse> restResult = productApi.sendRequestProductOrder(list);
    try {
      // 재고 응답 ...
      log.info("Order Product confirm Size {}", restResult.size());

      // 주문 정보 저장
      Order order = Order.builder()
          .requesterId(reqeust.getRequesterId())
          .receiverName(reqeust.getReceiverName())
          .contact(reqeust.getContact())
          .address(reqeust.getAddress())
          .memo(reqeust.getMemo())
          .orderTotalPrice(calculateTotalAmount(restResult))
          .orderStatus(OrderStatus.ORDER)
          .build();

      orderRepository.save(order);

      //주문 제품 정보 저장
      List<OrderProducts> products = restResult.stream().map(response -> {
        return OrderProducts.builder()
            .productId(response.getId())
            .productName(response.getName())
            .quantity(response.getQuantity())
            .amount(response.getAmount())
            .order(order)
            .build();
      }).toList();

      orderProductsRepository.saveAll(products);

      //주문 상태 정보 저장
      OrderSteps status = OrderSteps.builder().orderStep(OrderStep.ORDER_START).order(order)
          .build();

      // insert
      orderStepRepository.save(status);
      // 결과 맵핑
      return OrderResponse.of(order, products, List.of(status));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      handlingException(reqeust);
      throw new RestApiException(OrderError.ORDER_INSERT_ERROR);
    }
  }

  @MethodLogWriter
  @Transactional
  public OrderResponse updateOrder(final Long id, final OrderModifyRequest request) {

    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RestApiException(OrderError.PRODUCT_NOT_FOUND));

    // 이미 취소된 주문 체크
    if (OrderStatus.CANCEL.equals(order.getOrderStatus())) {
      throw new RestApiException(OrderError.ORDER_ALREADY_CANCEL);
    }

    log.debug("update Order Info");
    order.update(request);
    List<ProductOrderRequest> list = request.getProductModifyList();
    if (Objects.nonNull(list)) {
      log.debug("update Order Product Info");
      productApi.sendModifyProductOrder(list);
      //업데이트 처리
      request.getProductModifyList().forEach((modifyProduct) -> {
        OrderProducts products = orderProductsRepository.findById(modifyProduct.getId()).orElseThrow(() -> new RestApiException(OrderError.PRODUCT_NOT_FOUND));
        products.update(modifyProduct);
      });

      int updateAmount = orderProductsRepository.findAllByOrder(order).stream().mapToInt(OrderProducts::getTotalAmount).sum();;
      order.totalAmountUpdate(updateAmount);
    }

    return OrderResponse.from(order);
  }

  /**
   * 주문 취소
   *
   * @param id
   */
  @MethodLogWriter
  @Transactional
  public void cancelOrder(final Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RestApiException(OrderError.PRODUCT_NOT_FOUND));
    log.info("Order cancel id {}", id);
    log.debug("Order cancel update");
    order.cancelOrder();

    //상품 서비스에 데이터 요청....
    List<ProductOrderRequest> cancelRequests = order.getOrderProducts().stream().map((product) -> {
      return ProductOrderRequest.builder()
          .productId(product.getProductId())
          .reqQuantity(product.getOrderQuantity())
          .build();
    }).toList();
    productApi.sendDeleteProductOrder(cancelRequests);

    log.debug("Order Step insert");
    //주문 상태 테이블 이력 추가..
    orderStepRepository.save(
        OrderSteps.builder().order(order).orderStep(OrderStep.CANCELED).build());

  }


  private int calculateTotalAmount(final List<ProductOrderResponse> list) {
    return list.stream()
        .mapToInt(item -> item.getAmount() * item.getQuantity())
        .sum();
  }

  /**
   * 저장 실패시 ... 다시 product 서버로 취소 물량 날려줌...
   *
   * @param request
   */
  private void handlingException(final OrderRequest request) {
    List<ProductOrderRequest> cancleList =
        request.getProductList().stream().map((p) -> ProductOrderRequest.builder()
            .productId(p.getProductId())
            .reqQuantity(p.getQuantity()).build()
        ).toList();
    productApi.sendDeleteProductOrder(cancleList);
  }

}
