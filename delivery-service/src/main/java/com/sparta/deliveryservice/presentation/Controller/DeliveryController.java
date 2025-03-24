package com.sparta.deliveryservice.presentation.Controller;

import com.sparta.deliveryservice.application.dto.DeliveryInfoDto;
import com.sparta.deliveryservice.application.service.DeliveryService;
import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.domain.model.SortDto;
import com.sparta.deliveryservice.presentation.request.DeliveryRequest;
import com.sparta.deliveryservice.presentation.request.UpdateDeliveryRequest;
import com.sparta.deliveryservice.presentation.response.DeliveryCreatedResponse;
import com.sparta.deliveryservice.presentation.response.DeliveryInternalResponse;
import com.sparta.deliveryservice.presentation.response.DeliveryResponse;
import com.sparta.deliveryservice.presentation.response.UpdateDeliveryResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;


    @PostMapping
    public ResponseEntity<DeliveryCreatedResponse> createDelivery(
        @RequestBody DeliveryRequest deliveryRequest) {

        return deliveryService.createDelivery(deliveryRequest);
    }


    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable UUID deliveryId) {

        return deliveryService.deleteDelivery(deliveryId);
    }


    @PatchMapping("/{deliveryId}")
    public ResponseEntity<String> updateDelivery(@PathVariable UUID deliveryId,
        @RequestBody UpdateDeliveryRequest updateDeliveryRequest) {

        return deliveryService.updateDelivery(deliveryId, updateDeliveryRequest);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> getDelivery(@PathVariable UUID deliveryId
    ) {
        DeliveryResponse deliveries = deliveryService.getDelivery(deliveryId);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<DeliveryResponse>> getDeliveryByOrder(@PathVariable UUID orderId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "10") int size
    ) {
        SortDto sortDto = new SortDto(page, size, sortBy, direction);
        List<DeliveryResponse> deliveries = deliveryService.getDeliveryByOrder(orderId, sortDto);
        return ResponseEntity.ok(deliveries);
    }


    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> getDeliveries(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "10") int size
    ) {
        SortDto sortDto = new SortDto(page, size, sortBy, direction);
        List<DeliveryResponse> deliveries = deliveryService.getDeliveries(sortDto);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DeliveryResponse>> searchOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "") String keyword,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "10") int size
    ) {
        SearchDto searchDto = new SearchDto(page, keyword, sortBy, direction, size);

        List<DeliveryResponse> deliveries = deliveryService.searchDeliveries(searchDto);
        return ResponseEntity.ok(deliveries);
    }

}
