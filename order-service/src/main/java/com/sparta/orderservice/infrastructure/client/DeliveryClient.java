package com.sparta.orderservice.infrastructure.client;


import com.sparta.orderservice.presentation.requset.DeliveryRequest;
import com.sparta.orderservice.presentation.response.DeliveryCreatedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/api/deliveries")
    DeliveryCreatedResponse createDelivery(@RequestBody DeliveryRequest deliveryRequest);

}
