package com.sparta.deliveryservice.common;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode{
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류로 인해 요청을 처리할 수 없습니다."),

    INVALID_ROLE(HttpStatus.FORBIDDEN,"권한이 없습니다."),
    INVALID_Delivery(HttpStatus.BAD_REQUEST,"해당 배송이 없습니다.");

    private final HttpStatus status;
    private final String message;


    @Override
    public String getCode() {
        return this.name();
    }
}
