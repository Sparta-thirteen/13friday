package com.sparta.eureka.hub.infrastructure.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditableConfig {

    @Bean
    public AuditorAware<Long> auditorAware(HttpServletRequest request) {
        return new AuditorAwareImpl(request);
    }
}
