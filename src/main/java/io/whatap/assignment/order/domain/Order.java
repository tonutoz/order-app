package io.whatap.assignment.order.domain;

import io.whatap.assignment.cmm.domain.CommonEntity;
import io.whatap.assignment.cmm.exception.RestApiException;
import io.whatap.assignment.order.dto.OrderModifyRequest;
import io.whatap.assignment.order.dto.OrderRequest;
import io.whatap.assignment.order.exception.OrderError;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Table(name="ORDER_LIST")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends CommonEntity {

  @Id
  @Column(name="id")
  @Comment("주문 아이디")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "REQUESTER_ID")
  @Comment("주문자아이디")
  private String requesterId;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
  private List<OrderProducts> orderProducts = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
  private List<OrderSteps> orderSteps = new ArrayList<>();

  @Column(name = "RECEIVER_NAME")
  @Comment("수취인")
  private String receiverName;

  @Column(name = "CONTACT")
  @Comment("수취인 연락처")
  private String contact;

  @Column(name = "ADRESS")
  @Comment("수취인 주소")
  private String address;

  @Column(name = "MEMO")
  @Comment("배송 요청 사항")
  private String memo;

  @Column(name="TOTAL_PRICE", nullable = false)
  @Comment("주문 금액")
  private Integer orderTotalPrice;

  @Enumerated(value = EnumType.STRING)
  @Column(name="STATUS")
  @Comment("주문 상태(취소 여부)")
  private OrderStatus orderStatus;

  @Builder
  public Order(String requesterId, List<OrderProducts> orderProducts,
      List<OrderSteps> orderSteps,
      String receiverName, String contact, String address, String memo, Integer orderTotalPrice, OrderStatus orderStatus) {
    this.requesterId = requesterId;

    if(Objects.nonNull(orderProducts)) {
      this.orderProducts = orderProducts;
    }

    if(Objects.nonNull(orderSteps)) {
      this.orderSteps = orderSteps;
    }

    this.receiverName = receiverName;
    this.contact = contact;
    this.address = address;
    this.memo = memo;
    this.orderTotalPrice = orderTotalPrice;
    this.orderStatus = orderStatus;
  }

  public void update(final OrderModifyRequest orderModifyRequest) {

    if(Objects.nonNull(orderModifyRequest.getContact()) && !orderModifyRequest.getContact().equals(this.contact)) {
      this.contact = orderModifyRequest.getContact();
    }

    if(Objects.nonNull(orderModifyRequest.getReceiverName()) && !orderModifyRequest.getReceiverName().equals(this.receiverName)) {
      this.receiverName = orderModifyRequest.getReceiverName();
    }

    if(Objects.nonNull(orderModifyRequest.getMemo()) && !orderModifyRequest.getMemo().equals(this.memo)) {
      this.memo = orderModifyRequest.getMemo();
    }

    if(Objects.nonNull(orderModifyRequest.getAddress()) && !orderModifyRequest.getAddress().equals(this.address)) {
      this.address = orderModifyRequest.getAddress();
    }

  }

  public void totalAmountUpdate(final int totalPrice) {
    this.orderTotalPrice = totalPrice;
  }

  public void cancelOrder() {
    if(this.orderStatus.equals(OrderStatus.CANCEL)) {
      throw new RestApiException(OrderError.ORDER_ALREADY_CANCEL);
    }

    this.orderStatus = OrderStatus.CANCEL;

  }

}
