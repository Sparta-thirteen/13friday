package com.sparta.deliveryservice.presentation.advice;


import com.sparta.deliveryservice.common.CustomException;
import com.sparta.deliveryservice.common.ErrorResponse;
import com.sparta.deliveryservice.common.GlobalExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        log.warn("CustomException 발생 - code: {}, message: {}", ex.getHttpStatus(), ex.getErrorMessage());

        ErrorResponse response = new ErrorResponse(
            ex.getHttpStatus().value(),
            ex.getErrorMessage()
        );
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "잘못된 요청 파라미터 형식입니다."
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            GlobalExceptionCode.INTERNAL_ERROR.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}