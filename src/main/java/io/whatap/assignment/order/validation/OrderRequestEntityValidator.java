package io.whatap.assignment.order.validation;

import io.whatap.assignment.cmm.exception.ErrorResponse.ValidationError;
import io.whatap.assignment.order.dto.OrderRequest;
import io.whatap.assignment.order.dto.ProductRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * 주문 요청 벨리데이터
 */
@Component
public class OrderRequestEntityValidator implements RequestEntityValidator<OrderRequest> {

  private static final String IS_NULL = "is null";

  public List<ValidationError> validate(final OrderRequest request) {

    List<ValidationError> errorList = new ArrayList<>();

    if (Objects.isNull(request.getRequesterId())) {
      errorList.add(ValidationError.builder().field("requesterId").message(IS_NULL).build());
    }

    if (Objects.isNull(request.getReceiverName())) {
      errorList.add(ValidationError.builder().field("receiverName").message(IS_NULL).build());
    }

    if (Objects.isNull(request.getContact())) {
      errorList.add(ValidationError.builder().field("contact").message(IS_NULL).build());
    }

    if (Objects.isNull(request.getAddress())) {
      errorList.add(ValidationError.builder().field("address").message(IS_NULL).build());
    }

    if (request.getProductList().isEmpty()) {
      errorList.add(ValidationError.builder().field("productList").message(IS_NULL).build());
    } else {
      int index = 0;
      for (ProductRequest p : request.getProductList()) {
        if (Objects.isNull(p.getProductId())) {
          errorList.add(
              ValidationError.builder().field("productId " + "(" + index + ")").message(IS_NULL)
                  .build());
        }
        if (Objects.isNull(p.getQuantity())) {
          errorList.add(
              ValidationError.builder().field("quantity " + "(" + index+ ")").message(IS_NULL)
                  .build());
        }
        index++;
      }
    }

    return errorList;
  }

}
