package io.whatap.assignment.order.domain;

import io.whatap.assignment.cmm.domain.CommonEntity;
import io.whatap.assignment.order.product.dto.ProductOrderRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Table(name="ORDER_PRODUCTS")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProducts extends CommonEntity {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ORDER_ID")
  private Order order;

  @Column(name="PRODUCT_ID", nullable = false,unique = true)
  @Comment("주문 제품 아이디")
  private Long productId;

  @Column(name="PRODUCT_NAME", nullable = false)
  @Comment("주문 제품명")
  private String productName;

  @Column(name="PRODUCT_PRICE", nullable = false)
  @Comment("주문 제품 가격")
  private Integer amount;

  @Column(name="ORDER_QUANTITY", nullable = false)
  @Comment("주문 수량")
  private Integer orderQuantity;

  @Column(name="TOTAL_AMOUNT", nullable = false)
  @Comment("주문 제품 총 가격")
  private Integer totalAmount;

  @Builder
  public OrderProducts(Order order, Long productId,String productName, Integer amount, Integer quantity) {

    this.order = order;
    this.productId = productId;
    this.productName = productName;
    this.amount = amount;
    this.orderQuantity = quantity;
    this.totalAmount = amount*quantity;

  }

  public void update(final ProductOrderRequest request) {
    this.orderQuantity = request.getReqQuantity();
    this.totalAmount = request.getReqQuantity() *amount;
  }

}
