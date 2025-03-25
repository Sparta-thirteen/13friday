package com.sparta.eureka.hub.infrastructure.common.response;

import com.sparta.eureka.hub.infrastructure.common.exception.BusinessLogicException;
import com.sparta.eureka.hub.infrastructure.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }
}
