package com.sparta.deliveryservice.infrastructure.client;


import com.sparta.deliveryservice.application.dto.DeliveryRequestDto;
import com.sparta.deliveryservice.application.dto.ShippingManagerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("auth-service")
public interface ShippingManagerClient {

    @GetMapping("/api/courier/delivery")
    ShippingManagerResponseDto getSearchShippingManagers(
        @RequestBody DeliveryRequestDto deliveryRequestDto);
}