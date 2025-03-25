package com.sparta.deliveryservice.infrastructure.client;

import com.sparta.deliveryservice.application.dto.HubRouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("hub-service")
public interface HubClient {

    @PostMapping("/api/hub-route")
     HubRouteDto.ResponseDto createHubRoute(@RequestHeader("X-Role") String role,
        @RequestBody HubRouteDto.CreateDto request);

}
