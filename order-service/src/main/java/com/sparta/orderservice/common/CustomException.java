package com.sparta.orderservice.common;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }


    public String getErrorMessage() {
        return exceptionCode.getMessage();
    }

    public String getErrorCode() {
        return exceptionCode.getCode();
    }
}