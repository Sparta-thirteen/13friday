package com.sparta.slack_service.common.infrastructure.client;

import com.sparta.slack_service.common.infrastructure.dto.BaseResponse;
import com.sparta.slack_service.common.infrastructure.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("user-service")
public interface UserClient {

  @GetMapping("/api/users/{id}")
  ResponseEntity<BaseResponse<UserResponseDto>> getUser(@PathVariable Long id,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role);
}
