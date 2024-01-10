package io.whatap.assignment.order.dto;

import io.whatap.assignment.order.domain.OrderProducts;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResponse {

    private Long productId;

    /**
     * 제품명
     */
    private String productName;
    
    /**
     * 주문 수량
     */
    @NotNull
    private Integer quantity;

    /**
     * 주문 금액
     */
    @NotNull
    private Integer amount;

    @Builder
    public ProductResponse(Long productId,String productName, Integer quantity, Integer amount) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
    }

    public static ProductResponse from(final OrderProducts product) {
        return ProductResponse.builder()
            .productId(product.getProductId())
            .productName(product.getProductName())
            .amount(product.getAmount())
            .quantity(product.getOrderQuantity())
            .build();
    }

}
