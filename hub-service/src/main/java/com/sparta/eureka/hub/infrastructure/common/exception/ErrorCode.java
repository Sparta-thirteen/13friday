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
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 관리자를 찾을 수 없습니다."),
    HUB_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 삭제된 허브입니다."),
    HUB_CONFLICT(HttpStatus.CONFLICT, "이미 허브가 존재합니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "요청에 대한 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버가 요청을 처리할 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}
