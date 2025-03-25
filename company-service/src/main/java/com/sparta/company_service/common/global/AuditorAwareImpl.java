package com.sparta.company_service.common.global;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

@Slf4j
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

  private final HttpServletRequest request;

  @Override
  public Optional<Long> getCurrentAuditor() {
    String userIdHeader = request.getHeader("X-User-Id");

    if (userIdHeader == null) {
      log.warn("X-User-Id 헤더가 존재하지 않습니다.");
      return Optional.empty();
    }

    try {
      Long userId = Long.parseLong(userIdHeader);
      return Optional.of(userId);
    } catch (NumberFormatException e) {
      log.error("잘못된 X-User-Id 형식: {}", userIdHeader, e);
      return Optional.empty();
    }
  }
}
