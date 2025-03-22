package com.sparta.eureka.client.auth.domain.auth.dto.response;

import com.sparta.eureka.client.auth.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
  private String username;
  private String accessToken;
  private Role role;
}
