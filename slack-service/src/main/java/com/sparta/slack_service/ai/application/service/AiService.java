package com.sparta.slack_service.ai.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AiService {

  private final ObjectMapper objectMapper;
  @Value(value = "${gemini.api.key}")
  private String geminiApiKey;

  private final WebClient webClient;

  public String getShippingDeadline(String prompt) throws JsonProcessingException {
    // 요청 JSON 데이터 생성
    String request = prompt + "\n위 내용을 기반으로 최종 발송 시한을 계산해주세요. (날짜와 시간만 30자 이내로 답변)";
    String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + request + "\" }] }] }";

    // WebClient를 사용해 요청
    String response = webClient.post()
        .uri(uriBuilder -> uriBuilder.queryParam("key", geminiApiKey).build())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();

    // 응답 JSON에서 답변 추출
    return extractDeadline(response);
  }

  private String extractDeadline(String response) throws JsonProcessingException {
    JsonNode rootNode = objectMapper.readTree(response);
    return rootNode.path("candidates").get(0)
        .path("content").path("parts").get(0)
        .path("text").asText();
  }
}
