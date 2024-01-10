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
public enum OrderError implements ErrorType {

  ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ERR-02-001", "주문 정보를 찾을 수 없습니다."),
  ORDER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "ERR-02-002", "이미 등록되었습니다."),
  ORDER_ALREADY_CANCEL(HttpStatus.BAD_REQUEST, "ERR-02-003", "이미 취소된 주문입습니다."),
  ORDER_INSERT_ERROR(HttpStatus.BAD_REQUEST, "ERR-02-004", "주문 도중 에러가 발생하였습니다."),
  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ERR-02-005", "주문 제품 정보가 존재하지 않습니다."),
  PRODUCT_IS_SOLDOUT(HttpStatus.BAD_REQUEST, "ERR-02-006", "상품이 품절 되었습니다."),
  PRODUCT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "ERR-02-007", "상품이 재고가 충분치 않습니다."),
  ORDER_REQ_NOT_VALID(HttpStatus.BAD_REQUEST, "ERR-02-008", "데이터 요청이 잘못되었습니다."),
  ;

  private final HttpStatus httpStatus;

  private final String errorCode;

  private final String errorMsg;

  public static OrderError of(final String errorCode) {
    return Arrays.stream(values()).filter(t -> {
      return t.getErrorCode().equals(errorCode);
    }).findFirst().orElse(PRODUCT_IS_SOLDOUT);
  }

}
