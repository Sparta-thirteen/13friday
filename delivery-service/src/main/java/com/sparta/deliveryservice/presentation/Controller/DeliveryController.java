package com.sparta.deliveryservice.presentation.Controller;

import com.sparta.deliveryservice.application.service.DeliveryService;
import com.sparta.deliveryservice.presentation.request.DeliveryRequest;
import com.sparta.deliveryservice.presentation.request.UpdateDeliveryRequest;
import com.sparta.deliveryservice.presentation.response.UpdateDeliveryResponse;
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
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;


    @PostMapping
    public ResponseEntity<String> createDelivery(@RequestBody DeliveryRequest deliveryRequest) {

        return deliveryService.createDelivery(deliveryRequest);
    }



    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable UUID deliveryId) {

        return deliveryService.deleteDelivery(deliveryId);
    }




    @PatchMapping("/{deliveryId}")
    public ResponseEntity<String> updateDelivery(@PathVariable UUID deliveryId, @RequestBody UpdateDeliveryRequest updateDeliveryRequest) {

        return deliveryService.updateDelivery(deliveryId,updateDeliveryRequest);
    }
}
