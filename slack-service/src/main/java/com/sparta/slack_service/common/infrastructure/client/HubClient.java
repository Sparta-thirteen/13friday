package com.sparta.slack_service.common.infrastructure.client;

import com.sparta.slack_service.common.infrastructure.dto.HubDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient {

  @GetMapping("/{hubId}")
  ResponseEntity<HubDto.ResponseDto> getHub(@PathVariable("hubId") UUID hubId);
}
