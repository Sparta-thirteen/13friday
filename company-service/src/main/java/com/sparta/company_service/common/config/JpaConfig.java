package com.sparta.company_service.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

//  @Bean
//  public AuditorAware<Long> auditorProvider() {
//    return new AuditorAwareImpl();
//  }
}
