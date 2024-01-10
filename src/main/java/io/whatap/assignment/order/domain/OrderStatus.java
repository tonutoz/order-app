package io.whatap.assignment.order.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

  ORDER("쭈문완료"),
  CANCEL("주문취소");

  private final String desc;



}
