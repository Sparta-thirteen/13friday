package com.sparta.slack_service.slack.application.dto;

import lombok.Getter;

@Getter
public class SlackRequestDto {

  private Long receiverId;
  private String message;
}
