package com.sparta.eureka.client.auth.common.client;

import com.sparta.eureka.client.auth.common.client.dto.HubIdResponseDto;
import com.sparta.eureka.client.auth.common.client.dto.HubRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hub-service")
public interface HubClient {
  @PostMapping("/api/hub/grant/{userId}")
  void updateHub(@RequestHeader("X-Role") String role,
      @PathVariable("userId") Long userId,
      @RequestBody HubRequestDto requestDto);

  @GetMapping("/api/hub/manager")
  HubIdResponseDto getHubByManager(@RequestHeader("X-User-Id")String userId);
}
