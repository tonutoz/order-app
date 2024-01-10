package io.whatap.assignment.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRequest {

    /**
     * 주문 아이디
     */
    @NotNull
    private Long productId;

    /**
     * 주문 수량
     */
    @NotNull
    private Integer quantity;

    @Builder
    public ProductRequest(Long productId,Integer quantity) {
      this.productId = productId;
      this.quantity = quantity;
    }

}
