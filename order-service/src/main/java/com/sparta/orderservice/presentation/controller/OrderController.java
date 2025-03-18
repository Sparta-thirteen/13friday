package com.sparta.orderservice.presentation.controller;

import com.sparta.orderservice.application.service.OrderService;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import com.sparta.orderservice.presentation.response.UpdateOrderResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest req) {

        return orderService.createOrder(req);
    }


    // TODO: 유저아이디 필요
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {

        return orderService.cancelOrder(orderId,1L);
    }



    @PatchMapping("/{orderId}")
    public UpdateOrderResponse updateOrder(@PathVariable UUID orderId,@RequestBody UpdateOrderRequest req) {

        System.out.println("???? "+req.getOrderItemsRequests().get(0).getProductId());
        return orderService.updateOrder(orderId,req);
    }

}


