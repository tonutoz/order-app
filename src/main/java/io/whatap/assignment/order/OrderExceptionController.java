package io.whatap.assignment.order;

import io.whatap.assignment.cmm.exception.CommonError;
import io.whatap.assignment.cmm.exception.ErrorResponse;
import io.whatap.assignment.cmm.exception.ErrorResponse.ValidationError;
import io.whatap.assignment.cmm.exception.ErrorType;
import io.whatap.assignment.cmm.exception.RestApiException;
import io.whatap.assignment.order.exception.InvalidRequestException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class OrderExceptionController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<?> handleValidateException(InvalidRequestException e) {
    return handleExceptionInternal(e.getErrorType(), e.getValidateErrorList());
  }

  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<?> handleCustomException(RestApiException e) {
    return handleExceptionInternal(e.getErrorType());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("handleIllegalArgument", e);
    ErrorType errorCode = CommonError.INVALID_PARAMETER;
    return handleExceptionInternal(errorCode, e.getMessage());
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<?> handleAllException(Exception ex) {
    log.warn("handleAllException", ex);
    ErrorType errorType = CommonError.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(errorType);
  }

  private ResponseEntity<?> handleExceptionInternal(ErrorType errorType) {
    return ResponseEntity.status(errorType.getHttpStatus())
        .body(makeErrorResponse(errorType));
  }

  private ErrorResponse makeErrorResponse(ErrorType errorType) {
    return ErrorResponse.builder()
        .code(errorType.getErrorCode())
        .message(errorType.getErrorMsg())
        .build();
  }

  private ResponseEntity<?> handleExceptionInternal(ErrorType errorType, String message) {
    return ResponseEntity.status(errorType.getHttpStatus())
        .body(makeErrorResponse(errorType, message));
  }

  private ErrorResponse makeErrorResponse(ErrorType errorType, String message) {
    return ErrorResponse.builder()
        .code(errorType.getErrorCode())
        .message(message)
        .build();
  }

  private ResponseEntity<?> handleExceptionInternal(
      ErrorType errorType, List<ValidationError> validationErrorList) {
    return ResponseEntity.status(errorType.getHttpStatus())
        .body(makeErrorResponse(errorType,validationErrorList));
  }

  private ErrorResponse makeErrorResponse(ErrorType errorType, List<ValidationError> validationErrorList) {
    return ErrorResponse.builder()
        .code(errorType.getErrorCode())
        .message(errorType.getErrorMsg())
        .errors(validationErrorList)
        .build();
  }

}
