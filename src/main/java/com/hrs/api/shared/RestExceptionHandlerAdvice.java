package com.hrs.api.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hrs.api.shared.errors.ErrorCode;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
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
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestExceptionHandlerAdvice {

  @SneakyThrows
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public BaseEntityResponse entityNotFoundException(EntityNotFoundException e) {
    log.error("EntityNotFoundException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E004.getCode(), ErrorCode.E004.getMessage(), e.getMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseEntityResponse badRequestException(BadRequestException e) {
    log.error("BadRequestException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E002.getCode(), ErrorCode.E002.getMessage(), e.getMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public BaseEntityResponse forbiddenException(AccessDeniedException e) {
    log.error("AccessDeniedException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E003.getCode(), ErrorCode.E003.getMessage(), e.getMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseEntityResponse authenticateException(AuthenticationException e) {
    log.error("AuthenticationException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E001.getCode(), ErrorCode.E001.getMessage(), e.getMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseEntityResponse invalidPayloadException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException {}", e.getMessage(), e);
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
    log.error("Exception {}", e.getMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(ErrorCode.E005.getCode(), ErrorCode.E005.getMessage(), null, null));
  }

  @SneakyThrows
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseEntityResponse constraintViolationException(ConstraintViolationException e) {
    log.error("ConstraintViolationException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(
            ErrorCode.E007.getCode(), ErrorCode.E007.getMessage(), e.getMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseEntityResponse badCredentialsException(BadCredentialsException e) {
    log.error("BadCredentialsException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
        new BaseErrorResponse(ErrorCode.E008.getCode(), ErrorCode.E008.getMessage(), null, null));
  }

  @SneakyThrows
  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseEntityResponse dataIntegrityViolationException(DataIntegrityViolationException e) {
    log.error("DataIntegrityViolationException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
            new BaseErrorResponse(
                    ErrorCode.E009.getCode(), ErrorCode.E009.getMessage(), e.getLocalizedMessage(), null));
  }

  @SneakyThrows
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseEntityResponse illegalArgumentException(IllegalArgumentException e) {
    log.error("IllegalArgumentException {}", e.getMessage(), e);
    return BaseEntityResponse.error(
            new BaseErrorResponse(
                    ErrorCode.E010.getCode(), ErrorCode.E010.getMessage(), e.getLocalizedMessage(), null));
  }
}
