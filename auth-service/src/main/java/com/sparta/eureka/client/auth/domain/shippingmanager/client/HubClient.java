package com.sparta.eureka.client.auth.domain.shippingmanager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "hub-service")
public interface HubClient {
  //todo:hub서버에서 호출하기

//  @GetMapping("/api/hub")
}
