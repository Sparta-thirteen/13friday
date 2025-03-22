package com.sparta.eureka.hub.infrastructure.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),
    HUB_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 이동경로를 찾을 수 없습니다."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 메소드 요청입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버가 요청을 처리할 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}
