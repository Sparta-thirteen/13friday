package com.sparta.orderservice.common;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    HttpStatus getStatus();
    String getMessage();
    String getCode();


}
