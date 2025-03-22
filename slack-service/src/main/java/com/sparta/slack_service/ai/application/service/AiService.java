package com.sparta.slack_service.ai.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.slack_service.ai.application.dto.AiRequestDto;
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

  public String getShippingDeadline(AiRequestDto requestDto) throws JsonProcessingException {
    // 프롬프트 생성
    String prompt = String.format(
        "발송지 : %s, 도착지 : %s, 요청사항 : %s 기반으로 최종 발송 시한을 알려주세요. 날짜와 시간만 답변으로 주세요 (30자 이내)",
        requestDto.getSendHub(), requestDto.getReceiveHub(), requestDto.getRequestMessage()
    );

    // 요청 JSON 데이터 생성
    String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

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
