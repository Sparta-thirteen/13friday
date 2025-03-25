package com.sparta.orderservice.infrastructure.client;


import com.sparta.orderservice.application.dto.DeliveryInfoDto;
import com.sparta.orderservice.presentation.requset.DeliveryRequest;
import com.sparta.orderservice.presentation.response.DeliveryCreatedResponse;
import com.sparta.orderservice.presentation.response.DeliveryInternalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/api/deliveries")
    DeliveryCreatedResponse createDelivery(@RequestBody DeliveryRequest deliveryRequest);


    @PostMapping("/api/delivery-route/internal")
    DeliveryInternalResponse getDeliveryInfo(@RequestBody DeliveryInfoDto dto);
}
