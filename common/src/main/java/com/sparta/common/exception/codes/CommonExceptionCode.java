package com.sparta.common.exception.codes;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.sparta.common.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {
  INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류로 인해 요청을 처리할 수 없습니다."),
  INVALID_INPUT(BAD_REQUEST, "입력하신 데이터에 오류가 있습니다. 요청 내용을 확인하고 다시 시도해 주세요."),
  NOT_READABLE(BAD_REQUEST, "입력하신 데이터가 잘못된 형식입니다."),
  INVALID_REQUEST(BAD_REQUEST, "요청하신 값이 올바르지 않습니다. 요청 값을 확인해 주세요."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "요청한 리소스에 대한 권한이 없습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요."),
  INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "잘못된 폐이지 번호 입니다."),
  INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "잘못된 페이지 사이즈 입니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
