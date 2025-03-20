package com.sparta.eureka.hub.infrastructure.client.openRouteService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openRoute-service", url = "https://api.openrouteservice.org")
public interface OpenRouteServiceClient {
    @GetMapping("/v2/directions/driving-car")
    OpenRouteServiceResponse getRoute(@RequestParam("api_key") String apiKey,
                                      @RequestParam("start") String start,
                                      @RequestParam("end") String end
    );
}
