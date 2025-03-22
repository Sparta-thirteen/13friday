package com.sparta.eureka.client.auth.common.jpa;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

  private final HttpServletRequest request;

  @Override
  public Optional<Long> getCurrentAuditor() {
    try {
      Long userId = Long.parseLong(request.getHeader("X-User-Id")); // API Gateway에서 추가한 헤더
      return Optional.ofNullable(userId).or(() -> Optional.of(0L));
    } catch (Exception e) {
      return Optional.of(0L);
    }
  }
}
