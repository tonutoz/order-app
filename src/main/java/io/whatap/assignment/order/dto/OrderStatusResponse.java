package io.whatap.assignment.order.dto;

import io.whatap.assignment.cmm.domain.CommonEntity;
import io.whatap.assignment.order.domain.OrderSteps;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Getter
public class OrderStatusResponse extends CommonEntity {

  private Long id;

  private String orderStepDesc;

  @Builder
  public OrderStatusResponse(Long id,LocalDateTime createdOn, LocalDateTime modifiedOn, String orderStepDesc) {
    this.id = id;
    this.createdOn = createdOn;
    this.modifiedOn = modifiedOn;
    this.orderStepDesc = orderStepDesc;
  }

  public static OrderStatusResponse from(final OrderSteps status){

    log.debug("Status getOrder Id {}" , status.getId());
    log.debug("Status getOrderStepDesc {}" , status.getOrderStepDesc());

    return OrderStatusResponse.builder()
        .id(status.getId())
        .createdOn(status.getCreatedOn())
        .modifiedOn(status.getModifiedOn())
        .orderStepDesc(status.getOrderStepDesc())
        .build();
  }
}
