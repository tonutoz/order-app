package io.whatap.assignment.order.product.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOrderResponse {

  private Long id;

  private String name;

  private Integer amount;

  private Integer quantity;

  @Builder
  public ProductOrderResponse(Long id, String name, Integer amount, Integer quantity) {
    this.id = id;
    this.name = name;
    this.amount = amount;
    this.quantity = quantity;
  }


}
