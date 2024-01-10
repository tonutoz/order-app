package io.whatap.assignment.order.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class OrderRequest {

  private String requesterId;

  private String receiverName;
  private String contact;
  private String address;
  private String memo;

  private List<ProductRequest> productList;

  @Builder
  public OrderRequest(String requesterId,  String receiverName, String contact, String address, String memo,
      List<ProductRequest> productList) {
    this.requesterId = requesterId;
    this.receiverName = receiverName;
    this.contact = contact;
    this.address = address;
    this.memo = memo;
    this.productList = productList;
  }


}
