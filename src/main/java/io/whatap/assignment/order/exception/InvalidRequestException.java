package io.whatap.assignment.order.exception;

import io.whatap.assignment.cmm.exception.ErrorResponse.ValidationError;
import io.whatap.assignment.cmm.exception.ErrorType;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidRequestException extends RuntimeException {

  private final ErrorType errorType;

  private final List<ValidationError> validateErrorList;

}
