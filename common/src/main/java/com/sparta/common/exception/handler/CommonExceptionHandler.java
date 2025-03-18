//package com.sparta.common.exception.handler;
//
//import com.sparta.common.exception.ApiBusinessException;
//import com.sparta.common.exception.ExceptionCode;
//import com.sparta.common.exception.codes.CommonExceptionCode;
//import com.sparta.common.exception.dto.ExceptionResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import java.nio.file.AccessDeniedException;
//import java.util.Map;
//import java.util.stream.Collectors;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.ServletWebRequest;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.method.annotation.HandlerMethodValidationException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//
///**
// * 전역 예외를 처리하는 공통 예외 핸들러
// * ResponseEntityExceptionHandler를 상속하여 Spring MVC에서 발생하는 예외도 처리할 수 있음
// */
//@Log4j2(topic = "Global Exception")
//@RestControllerAdvice
//public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
//  /**
//   * 비즈니스 로직에서 발생하는 사용자 정의 예외 처리
//   * ApiBusinessException을 상속한 모든 예외가 여기에서 처리됨
//   */
//  @ExceptionHandler(ApiBusinessException.class)
//  public final ResponseEntity<ExceptionResponse> handleApiBusinessException(
//      ApiBusinessException exc, HttpServletRequest request) {
//
//    ExceptionCode exceptionCode = exc.getExceptionCode(); // 예외 코드 가져오기
//    ExceptionResponse response = createErrorResponse(request, exceptionCode, null); // 에러 응답 생성
//
//    logError(exc, exceptionCode); // 로그 기록
//
//    return ResponseEntity.status(exceptionCode.getStatus()).body(response); // HTTP 응답 반환
//  }
//
//  /**
//   * 예상치 못한 RuntimeException(런타임 예외) 처리
//   * 일반적으로 NullPointerException, IllegalArgumentException 등이 포함됨
//   */
//  @ExceptionHandler(RuntimeException.class)
//  public ResponseEntity<ExceptionResponse> handleRuntimeException(
//      RuntimeException exc, HttpServletRequest request) {
//
//    ExceptionCode exceptionCode = CommonExceptionCode.INTERNAL_ERROR; // 내부 서버 오류 코드
//    ExceptionResponse response = createErrorResponse(request, exceptionCode, null);
//
//    logError(exc, exceptionCode);
//
//    return ResponseEntity.status(exceptionCode.getStatus()).body(response);
//  }
//
//  /**
//   * 요청 데이터 검증(@Valid) 실패 시 발생하는 예외 처리
//   * - 예: DTO 필드의 @NotNull, @Size 등 유효성 검사가 실패한 경우
//   */
//  @Override
//  protected ResponseEntity<Object> handleMethodArgumentNotValid(
//      MethodArgumentNotValidException exc,
//      HttpHeaders headers,
//      HttpStatusCode status,
//      WebRequest request) {
//
//    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
//    ExceptionCode exceptionCode = CommonExceptionCode.INVALID_INPUT; // 유효성 검사 실패 코드
//
//    // 유효성 검사 오류 정보를 Map으로 변환
//    Map<String, String> details = exc.getBindingResult().getFieldErrors().stream()
//        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//
//    ExceptionResponse response = createErrorResponse(servletRequest, exceptionCode, details);
//
//    logError(exc, exceptionCode);
//
//    return ResponseEntity.status(exceptionCode.getStatus()).body(response);
//  }
//
//  /**
//   * JSON 요청 바디가 잘못된 경우 발생하는 예외 처리
//   * - 예: 잘못된 JSON 형식으로 요청을 보낸 경우
//   */
//  @Override
//  protected ResponseEntity<Object> handleHttpMessageNotReadable(
//      HttpMessageNotReadableException exc,
//      HttpHeaders headers,
//      HttpStatusCode status,
//      WebRequest request) {
//
//    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
//    ExceptionCode exceptionCode = CommonExceptionCode.NOT_READABLE; // 요청 바디 읽기 실패 코드
//
//    ExceptionResponse response =
//        createErrorResponse(servletRequest, exceptionCode, Map.of("message", exc.getMessage()));
//
//    logError(exc, exceptionCode);
//
//    return ResponseEntity.status(exceptionCode.getStatus()).body(response);
//  }
//
//  /**
//   * Spring의 HandlerMethodValidationException 처리
//   * - @Validated 어노테이션을 사용한 컨트롤러 메서드 파라미터 검증 실패 시 발생
//   */
//  @Override
//  protected ResponseEntity<Object> handleHandlerMethodValidationException(
//      HandlerMethodValidationException exc,
//      HttpHeaders headers,
//      HttpStatusCode status,
//      WebRequest request) {
//
//    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
//    ExceptionCode exceptionCode = CommonExceptionCode.INVALID_REQUEST; // 요청 파라미터 검증 실패 코드
//
//    // 파라미터 유효성 검사 오류 정보를 Map으로 변환
//    Map<String, String> details = createMethodValidationErrors(exc);
//
//    ExceptionResponse response = createErrorResponse(servletRequest, exceptionCode, details);
//
//    logError(exc, exceptionCode);
//
//    return ResponseEntity.status(exceptionCode.getStatus()).body(response);
//  }
//
//  /**
//   * 권한 부족(403 Forbidden) 예외 처리
//   * - API Gateway에서 JWT 검증을 통과했지만, 특정 리소스 접근 권한이 없는 경우 발생
//   */
//  @ExceptionHandler(AccessDeniedException.class)
//  @ResponseStatus(HttpStatus.FORBIDDEN)
//  public ResponseEntity<ExceptionResponse> handleAccessDeniedException(
//      RuntimeException exc, HttpServletRequest request) {
//
//    ExceptionCode exceptionCode = CommonExceptionCode.FORBIDDEN; // 접근 금지 코드
//    ExceptionResponse response = createErrorResponse(request, exceptionCode, null);
//
//    logError(exc, exceptionCode);
//
//    return ResponseEntity.status(exceptionCode.getStatus()).body(response);
//  }
//
//  /**
//   * 예외 응답 객체 생성
//   * @param request 요청 객체
//   * @param exceptionCode 예외 코드
//   * @param details 상세 오류 정보 (Optional)
//   * @return ExceptionResponse 객체
//   */
//  private ExceptionResponse createErrorResponse(
//      HttpServletRequest request, ExceptionCode exceptionCode, Map<String, String> details) {
//    return ExceptionResponse.builder()
//        .httpMethod(request.getMethod())  // HTTP 메서드 (GET, POST 등)
//        .httpStatus(exceptionCode.getStatus().value()) // HTTP 상태 코드
//        .errorCode(exceptionCode.getCode()) // 예외 코드
//        .message(exceptionCode.getMessage()) // 예외 메시지
//        .path(request.getRequestURI()) // 요청 경로
//        .details(details) // 추가 오류 정보
//        .build();
//  }
//
//  /**
//   * 예외 로그 기록
//   * @param exception 발생한 예외
//   * @param exceptionCode 예외 코드
//   */
//  private void logError(Exception exception, ExceptionCode exceptionCode) {
//    log.error(
//        "Exception occurred - Message: {}, Code: {}",
//        exception.getMessage(),
//        exceptionCode.getCode());
//  }
//
//  /**
//   * @Validated 파라미터 유효성 검사 실패 시, 오류 메시지 변환
//   * @param exc HandlerMethodValidationException
//   * @return 오류 메시지 Map
//   */
//  private Map<String, String> createMethodValidationErrors(HandlerMethodValidationException exc) {
//    return exc.getParameterValidationResults().stream()
//        .collect(
//            Collectors.toMap(
//                result -> result.getResolvableErrors().get(0).getCodes()[1], // 필드명
//                result -> result.getResolvableErrors().get(0).getDefaultMessage(), // 오류 메시지
//                (existing, replacement) -> existing)); // 중복 필드는 기존 값 유지
//  }
//}
//
