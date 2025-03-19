package com.sparta.deliveryservice.presentation.Controller;

import com.sparta.deliveryservice.application.service.DeliveryRouteService;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-route")
@RequiredArgsConstructor
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;


    @PostMapping
    public ResponseEntity<String> createDeliveryroute() {
        return deliveryRouteService.createDeliveryRoute();
    }


    @DeleteMapping("/{deliveryRouteId}")
    public ResponseEntity<String> deleteDeliveryroute(@PathVariable UUID deliveryRouteId) {
        return deliveryRouteService.deleteDeliveryRoute(deliveryRouteId);
    }


}
