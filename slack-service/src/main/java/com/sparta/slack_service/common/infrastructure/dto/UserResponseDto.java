package com.sparta.slack_service.common.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

  private Long id;

  private String username;

  private String slackId;

  private Role role;

}
