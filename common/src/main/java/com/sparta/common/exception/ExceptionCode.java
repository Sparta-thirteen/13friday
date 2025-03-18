package com.sparta.common.exception;

import org.springframework.http.HttpStatusCode;

public interface ExceptionCode {

  HttpStatusCode getStatus();
  String getMessage();
  String getCode();
}
