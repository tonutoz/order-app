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
public class OrderStepResponse extends CommonEntity {

  private Long id;

  private String orderStepDesc;

  @Builder
  public OrderStepResponse(Long id,LocalDateTime createdOn, LocalDateTime modifiedOn, String orderStepDesc) {
    this.id = id;
    this.createdOn = createdOn;
    this.modifiedOn = modifiedOn;
    this.orderStepDesc = orderStepDesc;
  }

  public static OrderStepResponse from(final OrderSteps status){

    return OrderStepResponse.builder()
        .id(status.getId())
        .createdOn(status.getCreatedOn())
        .modifiedOn(status.getModifiedOn())
        .orderStepDesc(status.getOrderStepDesc())
        .build();
  }
}
