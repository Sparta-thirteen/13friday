package com.sparta.orderservice.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {


    @GetMapping("api/hello")
    public String hello() {
        return "성공이야! 이제 자러가쟈";
    }
}
