package io.whatap.assignment.order.exception;

import io.whatap.assignment.cmm.exception.ErrorType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString()
@RequiredArgsConstructor
public enum ProductError implements ErrorType {

  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ERR-01-002", "상품이 존재하지 않습니다."),
  PRODUCT_IS_SOLDOUT(HttpStatus.BAD_REQUEST, "ERR-01-003", "상품이 품절 되었습니다."),
  PRODUCT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "ERR-01-004", "상품이 재고가 충분치 않습니다."),;

  private final HttpStatus httpStatus;

  private final String errorCode;

  private final String errorMsg;

  public static ProductError of(final String errorCode) {
    return Arrays.stream(values()).filter(t-> t.getErrorCode().equals(errorCode)).findFirst().orElse(PRODUCT_NOT_FOUND);
  }

}
