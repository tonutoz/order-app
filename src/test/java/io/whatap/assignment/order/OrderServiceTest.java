package io.whatap.assignment.order;

import io.whatap.assignment.order.product.dto.ProductOrderResponse;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

  public static List<ProductOrderResponse> list;

  @BeforeAll
  public static void setupList() {
    list = IntStream.rangeClosed(0,3).boxed().map(i-> {
      return ProductOrderResponse.builder().amont(1000).quentity(3).build();
    }).toList();
  }


  @DisplayName("총금액 합계 테스트")
  @Test
  public void reduceTest() {

    System.out.println("size = " + list.size());
    int totalAmount = list.stream()
        .mapToInt(item -> item.getAmount() * item.getQuantity())
        .sum();
    System.out.println("total amount =" + totalAmount);
  }

}
