package io.whatap.assignment.order.dto;

import io.whatap.assignment.cmm.domain.CommonDto;
import io.whatap.assignment.order.domain.Order;
import io.whatap.assignment.order.domain.OrderProducts;
import io.whatap.assignment.order.domain.OrderSteps;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class OrderResponse extends CommonDto {

  private final Long id;
  private final String requesterId;
  private final String contact;
  private final String address;
  private final String memo;
  private final Integer orderTotalPrice;

  private List<ProductResponse> orderProductList;

  private List<OrderStepResponse> orderStatuseList;

  @Builder
  public OrderResponse(Long id, LocalDateTime createdOn, LocalDateTime modifiedOn,
      String requesterId, String contact,
      String address, String memo, Integer orderTotalPrice, List<ProductResponse> orderProductList, List<OrderStepResponse> orderStatuseList) {
    this.id = id;
    this.createdOn = createdOn;
    this.modifiedOn = modifiedOn;
    this.requesterId = requesterId;
    this.contact = contact;
    this.address = address;
    this.memo = memo;
    this.orderTotalPrice = orderTotalPrice;

    if(Objects.nonNull(orderProductList)) {
      this.orderProductList = orderProductList;
    }

    if(Objects.nonNull(orderStatuseList)) {
      this.orderStatuseList = orderStatuseList;
    }

  }

  public static OrderResponse from(final Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .createdOn(order.getCreatedOn())
        .modifiedOn(order.getModifiedOn())
        .requesterId(order.getRequesterId())
        .contact(order.getContact())
        .address(order.getAddress())
        .memo(order.getMemo())
        .orderTotalPrice(order.getOrderTotalPrice())
        .orderProductList(order.getOrderProducts().stream().map(ProductResponse::from).toList())
        .orderStatuseList(order.getOrderSteps().stream().map(OrderStepResponse::from).toList())
        .build();
  }

  public static OrderResponse of(final Order order,List<OrderProducts> productList, final List<OrderSteps> statusList) {
    return OrderResponse.builder()
        .id(order.getId())
        .createdOn(order.getCreatedOn())
        .modifiedOn(order.getModifiedOn())
        .requesterId(order.getRequesterId())
        .contact(order.getContact())
        .address(order.getAddress())
        .memo(order.getMemo())
        .orderTotalPrice(order.getOrderTotalPrice())
        .orderProductList(productList.stream().map(ProductResponse::from).toList())
        .orderStatuseList(statusList.stream().map(OrderStepResponse::from).toList())
        .build();
  }

}
