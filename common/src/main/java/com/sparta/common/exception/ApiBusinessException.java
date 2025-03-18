package com.sparta.common.exception;

import lombok.Getter;

@Getter
public class ApiBusinessException extends RuntimeException {
  private final ExceptionCode exceptionCode;

  public ApiBusinessException(ExceptionCode exceptionCode){
    super(exceptionCode.getMessage());
    this.exceptionCode = exceptionCode;
  }
}
