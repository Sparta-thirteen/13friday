package com.sparta.deliveryservice.common;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    HttpStatus getStatus();
    String getMessage();
    String getCode();


}
