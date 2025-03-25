package com.sparta.slack_service.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .baseUrl(GEMINI_URL)
        .defaultHeader("Content-Type", "application/json")
        .build();
  }
}
