package io.whatap.assignment.order;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.whatap.assignment.cmm.aop.ExecutionTimeChecker;
import io.whatap.assignment.cmm.exception.ErrorResponse.ValidationError;
import io.whatap.assignment.order.dto.OrderModifyRequest;
import io.whatap.assignment.order.dto.OrderRequest;
import io.whatap.assignment.order.dto.OrderResponse;
import io.whatap.assignment.order.exception.InvalidRequestException;
import io.whatap.assignment.order.exception.OrderError;
import io.whatap.assignment.order.validation.OrderRequestEntityValidator;
import io.whatap.assignment.order.validation.RequestEntityValidator;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="주문 REST API", description = "주문 CRUD용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

  private final OrderService orderService;

  private final RequestEntityValidator<OrderRequest> createValidator;

  private final RequestEntityValidator<OrderModifyRequest> modifyValidator;

  @ExecutionTimeChecker
  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getOrder(@PathVariable final Long id){
    log.info("id {}" , id);
    return ResponseEntity.ok(orderService.getOrder(id));
  }

  @ExecutionTimeChecker
  @GetMapping()
  public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam(name="memberId") final String requestId){
    log.info("request {}" ,requestId);
    List<OrderResponse> results= orderService.getOrderList(requestId);
    return ResponseEntity.ok(results);
  }

  @ExecutionTimeChecker
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> requestOrder(@RequestBody @Valid final OrderRequest request) {
    log.info("{}",request);

    List<ValidationError> validationErrorList = createValidator.validate(request);

    if(!validationErrorList.isEmpty()) {
      log.debug("validation Error size {}", validationErrorList.size());
      throw new InvalidRequestException(OrderError.ORDER_REQ_NOT_VALID,validationErrorList);
    }

    OrderResponse response = orderService.createOrder(request);
    return ResponseEntity.ok(response);
  }

  @ExecutionTimeChecker
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<OrderModifyRequest> updateOrder(@PathVariable final Long id, @RequestBody @Valid final OrderModifyRequest request) {
    List<ValidationError> validationErrorList = modifyValidator.validate(request);

    if(!validationErrorList.isEmpty()) {
      log.debug("validation Error size {}", validationErrorList.size());
      throw new InvalidRequestException(OrderError.ORDER_REQ_NOT_VALID,validationErrorList);
    }

    return ResponseEntity.ok(orderService.updateOrder(id,request));
  }

  @ExecutionTimeChecker
  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public void cancelOrder(@PathVariable final Long id){
    orderService.cancelOrder(id);
  }
}
