package com.sparta.slack_service.slack.application.dto;

import com.sparta.slack_service.slack.domain.entity.Slacks;
import lombok.Getter;

@Getter
public class SlackRequestDto {

  private String receiverEmail;
  private String message;

  public Slacks toEntity(String channelId, String sentAt) {
    return Slacks.builder()
        .message(this.message)
        .channelId(channelId)
        .sentAt(sentAt)
        .build();
  }
}
