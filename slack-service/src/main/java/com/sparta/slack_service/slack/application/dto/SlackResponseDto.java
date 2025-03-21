package com.sparta.slack_service.slack.application.dto;

import com.sparta.slack_service.slack.domain.entity.Slacks;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackResponseDto {

  private String message;
  private LocalDateTime sentAt;

  public static SlackResponseDto toDto(Slacks slacks) {
    return SlackResponseDto.builder()
        .message(slacks.getMessage())
        .sentAt(slacks.getCreatedAt())
        .build();
  }
}
