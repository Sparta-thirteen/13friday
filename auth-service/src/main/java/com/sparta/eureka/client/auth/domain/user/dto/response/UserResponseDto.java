package com.sparta.eureka.client.auth.domain.user.dto.response;

import com.sparta.eureka.client.auth.domain.user.entity.Role;
import com.sparta.eureka.client.auth.domain.user.entity.User;
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

  public static UserResponseDto fromEntity(User user) {
    return new UserResponseDto(
        user.getUserId(), user.getUsername(), user.getSlackId(), user.getRole());
  }
}
