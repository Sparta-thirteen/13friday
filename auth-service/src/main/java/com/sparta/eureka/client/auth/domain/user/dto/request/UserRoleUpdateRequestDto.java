package com.sparta.eureka.client.auth.domain.user.dto.request;

import com.sparta.eureka.client.auth.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateRequestDto {
  private Role role;
}
