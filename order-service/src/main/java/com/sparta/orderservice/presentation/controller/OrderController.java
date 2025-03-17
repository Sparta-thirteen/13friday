package com.sparta.orderservice.presentation.controller;

import com.sparta.orderservice.application.service.OrderService;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping("/api/order")
    public ResponseEntity<String> createOrder(OrderRequest req) {


        return orderService.createOrder(req);
    }

}


