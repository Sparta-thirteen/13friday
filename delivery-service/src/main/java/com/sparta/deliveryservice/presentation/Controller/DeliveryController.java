package com.sparta.deliveryservice.presentation.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {


    @PostMapping()
    public ResponseEntity<String> createDelivery() {



        return "성공이야! 이제 자러가쟈";
    }
}
