package com.sparta.eureka.client.auth.common.client;

import com.sparta.eureka.client.auth.common.client.dto.HubIdResponseDto;
import com.sparta.eureka.client.auth.common.client.dto.HubRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-service")
public interface HubClient {
  @PatchMapping("/api/hub/grant/{userId}")
  void updateHub(@RequestHeader("X-Role") String role,
      @PathVariable("userId") Long userId,
      @RequestBody HubRequestDto requestDto);

  @GetMapping("/api/hub/manger")
  HubIdResponseDto getHubByManager(@RequestHeader("X-User-Id")String userId);
}
