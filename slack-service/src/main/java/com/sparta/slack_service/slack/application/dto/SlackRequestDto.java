package com.sparta.slack_service.slack.application.dto;

import com.sparta.slack_service.slack.domain.entity.Slacks;
import lombok.Getter;

@Getter
public class SlackRequestDto {

  // user, order service 연결 후 수정 예정
  private String receiverEmail;
  //  private UUID orderId;
  private String message;
  private String sendHub;
  private String receiveHub;

  public Slacks toEntity(String channelId, String sentAt) {
    return Slacks.builder()
        .message(this.message)
        .channelId(channelId)
        .sentAt(sentAt)
        .build();
  }
}
