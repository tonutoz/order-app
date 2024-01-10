package io.whatap.assignment.order.dto;

import io.whatap.assignment.order.product.dto.ProductOrderRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class OrderModifyRequest {

  private String receiverName;
  private String contact;
  private String address;
  private String memo;

  private List<ProductOrderRequest> productModifyList;

  @Builder
  public OrderModifyRequest(String receiverName, String contact, String address, String memo,
      List<ProductOrderRequest> productModifyList) {
    this.receiverName = receiverName;
    this.contact = contact;
    this.address = address;
    this.memo = memo;
    this.productModifyList = productModifyList;
  }


}
