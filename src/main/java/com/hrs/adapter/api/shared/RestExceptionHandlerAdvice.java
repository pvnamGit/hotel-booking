package com.hrs.adapter.api.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hrs.adapter.api.shared.errors.ErrorCode;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestExceptionHandlerAdvice {

  private final Logger logger = LoggerFactory.getLogger(RestExceptionHandlerAdvice.class);

  @SneakyThrows
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public BaseEntityResponse entityNotFoundException(EntityNotFoundException e) {
    logger.error("EntityNotFoundException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E004.getCode(), ErrorCode.E004.getMessage(), e.getLocalizedMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseEntityResponse badRequestException(BadRequestException e) {
    logger.error("BadRequestException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E002.getCode(), ErrorCode.E002.getMessage(), e.getLocalizedMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public BaseEntityResponse forbiddenException(AccessDeniedException e) {
    logger.error("AccessDeniedException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E003.getCode(), ErrorCode.E003.getMessage(), e.getLocalizedMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseEntityResponse authenticateException(AuthenticationException e) {
    logger.error("AuthenticationException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E001.getCode(), ErrorCode.E001.getMessage(), e.getLocalizedMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseEntityResponse invalidPayloadException(MethodArgumentNotValidException e) {
    logger.error("MethodArgumentNotValidException {}", e.getLocalizedMessage(), e);
    List<String> errors =
        e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
    return BaseEntityResponse.error(
        new BaseErrorResponse(ErrorCode.E006.getCode(), ErrorCode.E006.getMessage(), null, errors));
  }

  @SneakyThrows
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseEntityResponse generalException(Exception e) {
    logger.error("Exception {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(ErrorCode.E005.getCode(), ErrorCode.E005.getMessage(), null, null));
  }

  @SneakyThrows
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseEntityResponse constraintViolationException(ConstraintViolationException e) {
    logger.error("ConstraintViolationException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E007.getCode(), ErrorCode.E007.getMessage(), e.getLocalizedMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseEntityResponse badCredentialsException(BadCredentialsException e) {
    logger.error("BadCredentialsException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(ErrorCode.E008.getCode(), ErrorCode.E008.getMessage(), null, null));
  }

  @SneakyThrows
  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseEntityResponse dataIntegrityViolationException(DataIntegrityViolationException e) {
    logger.error("DataIntegrityViolationException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
            new BaseErrorResponse(
                    ErrorCode.E009.getCode(), ErrorCode.E009.getMessage(), e.getLocalizedMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseEntityResponse illegalArgumentException(IllegalArgumentException e) {
    logger.error("IllegalArgumentException {}", e.getLocalizedMessage(), e);
    return BaseEntityResponse.error(
            new BaseErrorResponse(
                    ErrorCode.E010.getCode(), ErrorCode.E010.getMessage(), e.getLocalizedMessage(), null));
  }
}
