//package com.sparta.common.jpa;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AuditorAwareImpl implements AuditorAware<String> {
//
//  private final HttpServletRequest request;
//
//  @Override
//  public Optional<String> getCurrentAuditor() {
//    try {
//      String username = request.getHeader("X-User-Id"); // API Gateway에서 추가한 헤더
//      return Optional.ofNullable(username).or(() -> Optional.of("system"));
//    } catch (Exception e) {
//      return Optional.of("system");
//    }
//  }
//}
