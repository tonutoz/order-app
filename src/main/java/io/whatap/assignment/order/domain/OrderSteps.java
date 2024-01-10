package io.whatap.assignment.order.domain;

import io.whatap.assignment.cmm.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

@ToString
@Entity
@Table(name="ORDER_STEPS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderSteps extends CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  @Comment("아이디")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ORDER_ID")
  private Order order;

  @Enumerated(value = EnumType.STRING)
  @Column(name="STEP")
  @Comment("주문 단계")
  private OrderStep orderStep;

  @Column(name="STEP_DESC")
  @Comment("주문 단계")
  private String orderStepDesc;

  @Builder
  public OrderSteps(Order order, OrderStep orderStep) {
    this.order = order;
    this.orderStep = orderStep;
    this.orderStepDesc = orderStep.getDesc();
  }
}
