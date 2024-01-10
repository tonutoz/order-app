package io.whatap.assignment.order.validation;

import io.whatap.assignment.cmm.exception.ErrorResponse.ValidationError;
import io.whatap.assignment.order.dto.OrderModifyRequest;
import io.whatap.assignment.order.dto.OrderRequest;
import io.whatap.assignment.order.dto.ProductRequest;
import io.whatap.assignment.order.product.dto.ProductOrderRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * 주문 변경 요청 벨리데이터
 */
@Component
public class ModifyRequestEntityValidator implements RequestEntityValidator<OrderModifyRequest> {

  private static final String IS_NULL = "is null";

  public List<ValidationError> validate(final OrderModifyRequest request) {

    List<ValidationError> errorList = new ArrayList<>();

    List<ProductOrderRequest> productModifyList = request.getProductModifyList();
    if (!productModifyList.isEmpty()) {
      int index = 0;
      for (ProductOrderRequest p : productModifyList) {
        if (Objects.isNull(p.getId())) {
          errorList.add(
              ValidationError.builder().field("id " + "(" + index + ")").message(IS_NULL)
                  .build());
        }

        if (Objects.isNull(p.getProductId())) {
          errorList.add(
              ValidationError.builder().field("productId " + "(" + index + ")").message(IS_NULL)
                  .build());
        }

        if (Objects.isNull(p.getOriQuantity())) {
          errorList.add(
              ValidationError.builder().field("oriQuantity " + "(" + index + ")").message(IS_NULL)
                  .build());
        }
        if (Objects.isNull(p.getReqQuantity())) {
          errorList.add(
              ValidationError.builder().field("reqQuantity " + "(" + index + ")").message(IS_NULL)
                  .build());
        }
      }
      index++;
    }

    return errorList;
  }

}
