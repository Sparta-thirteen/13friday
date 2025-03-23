package com.sparta.orderservice.presentation.controller;

import com.sparta.orderservice.application.dto.SortDto;
import com.sparta.orderservice.application.service.OrderService;
import com.sparta.orderservice.common.CustomException;
import com.sparta.orderservice.common.GlobalExceptionCode;
import com.sparta.orderservice.domain.model.SearchDto;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import com.sparta.orderservice.presentation.response.OrderResponse;
import com.sparta.orderservice.presentation.response.UpdateOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "주문 API")
public class OrderController {


    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest req) {


      // TODO:  request로 공급,수령 업체 이름, details, 아이템(이름,수량) 으로 수정


        return orderService.createOrder(req);
    }


    // TODO: 유저아이디 필요
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {

        return orderService.cancelOrder(orderId, 1L);
    }


    @PatchMapping("/{orderId}")
    public UpdateOrderResponse updateOrder(@PathVariable UUID orderId,
        @RequestBody UpdateOrderRequest req) {

        return orderService.updateOrder(orderId, req);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {

        return orderService.getOrder(orderId);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        SortDto sortDto = new SortDto(page, size, sortBy, direction);
        List<OrderResponse> orders = orderService.getOrders(sortDto);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/search")
    public ResponseEntity<List<OrderResponse>> searchOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "10") int size
    ) {
        SearchDto searchDto = new SearchDto(keyword, sortBy, direction, size);

        List<OrderResponse> orders = orderService.searchOrders(page, searchDto);
        return ResponseEntity.ok(orders);
    }


}


