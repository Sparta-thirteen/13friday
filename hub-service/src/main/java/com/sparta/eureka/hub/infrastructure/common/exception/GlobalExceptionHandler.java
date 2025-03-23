package com.sparta.eureka.hub.infrastructure.common.exception;

import com.sparta.eureka.hub.infrastructure.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException e) {
        logger.error("ERROR ::: [RuntimeException] ", e);

        return ResponseEntity.status(
                e.getErrorCode().getStatus())
                .body(ErrorResponse.from(e.getErrorCode(),
                e.getErrorCode().getMessage()));
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ErrorResponse handleAllExceptions(Exception e) {
        logger.error("ERROR ::: [AllException] ", e);

        return ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
