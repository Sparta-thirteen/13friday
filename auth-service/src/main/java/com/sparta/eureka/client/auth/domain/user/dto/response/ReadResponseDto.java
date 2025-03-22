package com.sparta.eureka.client.auth.domain.user.dto.response;


import com.sparta.eureka.client.auth.domain.user.entity.Role;
import com.sparta.eureka.client.auth.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadResponseDto {
  private Long id;
  private String username;
  private String slackId;
  private Role role;
  private LocalDateTime createdAt;

  public static ReadResponseDto fromEntity(User user) {
    return ReadResponseDto.builder()
        .id(user.getUserId())
        .username(user.getUsername())
        .slackId(user.getSlackId())
        .role(user.getRole())
        .createdAt(user.getCreatedAt())
        .build();
  }
}
