package com.sparta.deliveryservice.presentation.Controller;

import com.sparta.deliveryservice.application.service.DeliveryRouteService;

import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.domain.model.SortDto;

import com.sparta.deliveryservice.presentation.request.UpdateDeliveryRequest;
import com.sparta.deliveryservice.presentation.response.DeliveryRouteResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-route")
@RequiredArgsConstructor
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;


//    @PostMapping
//    public ResponseEntity<String> createDeliveryRoute() {
//        return deliveryRouteService.createDeliveryRoute();
//    }

    @PostMapping("/redis")
    public ResponseEntity<String> createDeliveryRoutes() {
      deliveryRouteService.createDeliveryRoutes(UUID.randomUUID(),UUID.fromString("024c5663-7538-4421-9e97-109bea28d1c6"),UUID.fromString("49a40c61-d672-4f6a-9edf-d8f2e05440c4"),"인천 백범로123");
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


}
