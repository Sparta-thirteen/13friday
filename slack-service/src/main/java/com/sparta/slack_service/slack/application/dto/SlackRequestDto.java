package com.sparta.slack_service.slack.application.dto;

import com.sparta.slack_service.slack.domain.entity.Slacks;
import lombok.Getter;

@Getter
public class SlackRequestDto {

  // user, order service 연결 후 수정 예정
  private String receiverEmail;
  //  private UUID orderId;
  private String message;

  public Slacks toEntity() {
    return Slacks.builder()
        .message(this.message)
        .build();
  }
}
