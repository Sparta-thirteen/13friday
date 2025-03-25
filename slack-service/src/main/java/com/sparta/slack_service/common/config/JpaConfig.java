package com.sparta.slack_service.common.config;

import com.sparta.slack_service.common.global.AuditorAwareImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  public AuditorAware<Long> auditorProvider(HttpServletRequest request) {
    return new AuditorAwareImpl(request);
  }
}
