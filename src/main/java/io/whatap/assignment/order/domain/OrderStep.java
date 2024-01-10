package io.whatap.assignment.order.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum OrderStep {

  ORDER_START(1,"주문 완료"),
  PAYMENT_COMPLETE(2,"결제 완료"),
  PRODUCT_PREPARING(3,"상품 준비중"),
  SHIPPING(4,"출고 완료 배송중"),
  DELIVERY_COMPLETED(5,"배송 완료"),
  CANCELED(6,"취소"),
  ;

  private final int step;

  private final String desc;

  public static OrderStep of(int i) {
    return Arrays.stream(values()).filter((t) -> t.step == i).findFirst().orElseThrow(()-> new IllegalArgumentException(" Illegal Step"));
  }


}
