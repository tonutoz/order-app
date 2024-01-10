package io.whatap.assignment.order.validation;

import io.whatap.assignment.cmm.exception.ErrorResponse.ValidationError;
import java.util.List;

/**
 * Request Entity 검증 인터페이스
 * @param <T>
 */
public interface RequestEntityValidator<T>{

  public List<ValidationError> validate(T request);

}
