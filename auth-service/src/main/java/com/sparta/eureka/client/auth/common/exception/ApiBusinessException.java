package com.sparta.eureka.client.auth.common.exception;

import lombok.Getter;

@Getter
public class ApiBusinessException extends RuntimeException {
  private final ExceptionCode exceptionCode;
  private String userId;
  private String role;

  public ApiBusinessException(ExceptionCode exceptionCode){
    super(exceptionCode.getMessage());
    this.exceptionCode = exceptionCode;
  }

  public ApiBusinessException(ExceptionCode exceptionCode, String userId, String role) {
    super(exceptionCode.getMessage());
    this.exceptionCode = exceptionCode;
    this.userId = userId;
    this.role = role;
  }
}
