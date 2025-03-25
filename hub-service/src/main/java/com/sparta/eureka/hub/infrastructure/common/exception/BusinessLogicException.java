package com.sparta.eureka.hub.infrastructure.common.exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BusinessLogicException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
