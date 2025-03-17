package com.sparta.orderservice.presentation.controller;

import com.sparta.orderservice.application.service.OrderService;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping("/api/order")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest req) {

        System.out.println(req.getRequestDetails());
        return orderService.createOrder(req);
    }


    @DeleteMapping("/api/order/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {


        return orderService.deleteOrder(orderId);
    }



    @GetMapping("/test")
    public String aa() {

        return "오케이~";
    }

}


