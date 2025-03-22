package com.sparta.slack_service.ai.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiRequestDto {

  // order 정보 받아서 수정 예정
  private String sendHub;
  private String receiveHub;
  private String requestMessage;
}
