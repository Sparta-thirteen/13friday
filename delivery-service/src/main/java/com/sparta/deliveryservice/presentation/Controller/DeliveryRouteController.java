package com.sparta.deliveryservice.presentation.Controller;

import com.sparta.deliveryservice.application.dto.DeliveryInfoDto;
import com.sparta.deliveryservice.application.dto.DeliveryRoutesDto;
import com.sparta.deliveryservice.application.service.DeliveryRouteService;

import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.domain.model.SortDto;

import com.sparta.deliveryservice.presentation.response.DeliveryInternalResponse;
import com.sparta.deliveryservice.presentation.response.DeliveryRouteResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-route")
@RequiredArgsConstructor
@Slf4j
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;


    @PostMapping("/redis")
    public ResponseEntity<String> createDeliveryRoutes() {
        DeliveryRoutesDto dto= deliveryRouteService.createDeliveryRoutes(UUID.randomUUID(),
            UUID.fromString("9cf0ebfc-cdfa-4eba-9da4-40e527a25f13"), UUID.fromString("5b413b9d-fbdc-4dd5-b940-d9aeee534268"), "부산 동구 중앙대로 206","수령업체A");

        return ResponseEntity.ok("성공!!");
    }





    @DeleteMapping("/{deliveryRouteId}")
    public ResponseEntity<String> deleteDeliveryRoute(@PathVariable UUID deliveryRouteId) {
        return deliveryRouteService.deleteDeliveryRoute(deliveryRouteId);
    }


    @GetMapping("/{deliveryRouteId}")
    public ResponseEntity<DeliveryRouteResponse> getDeliveryRoute(
        @PathVariable UUID deliveryRouteId) {

        return deliveryRouteService.getDeliveryRoute(deliveryRouteId);
    }

    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<List<DeliveryRouteResponse>> getDeliveryRouteByDeliveryAddress(
        @PathVariable UUID deliveryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "10") int size) {

        SortDto sortDto = new SortDto(page, size, sortBy, direction);

        return deliveryRouteService.getDeliveryRouteByDeliveryAddress(deliveryId, sortDto);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryRouteResponse>> getAllDeliveryRoute(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "10") int size) {

        SortDto sortDto = new SortDto(page, size, sortBy, direction);

        return deliveryRouteService.getAllDeliveryRoute(sortDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DeliveryRouteResponse>> searchOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "") String keyword,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "10") int size
    ) {
        SearchDto searchDto = new SearchDto(page, keyword, sortBy, direction, size);
        List<DeliveryRouteResponse> deliveries = deliveryRouteService.searchDeliveryRoute(
            searchDto);
        return ResponseEntity.ok(deliveries);
    }

    @PostMapping("/internal")
    DeliveryInternalResponse getDeliveryInfo(@RequestBody DeliveryInfoDto dto) {

        return deliveryRouteService.getDeliveryInfo(dto);
    }


}
