package com.sparta.orderservice.domain.service;

import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.domain.model.OrderItems;
import com.sparta.orderservice.domain.model.SearchDto;
import com.sparta.orderservice.infrastructure.repository.JpaOrderRepository;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDomainService {

    private final JpaOrderRepository jpaOrderRepository;

    public Order createOrder(OrderRequest req) {

        // Order 엔티티 생성
        Order order = new Order(req.getSuppliersId(), req.getRecipientsId(), req.getDeliveryId(),
            req.getRequestDetails());

        // TODO: 재고 비교 값 필요
        int stockQuantity = 10000;
        for (OrderItemsRequest request : req.getOrderItemsRequests()) {
            OrderItems orderItems = new OrderItems(request.getProductId(), request.getName(),
                request.getStock());
            order.addOrderItem(orderItems);
            validateProductStock(stockQuantity, order.getTotalStock());
        }
        return order;
    }

    public Order updateOrder(Order order, UpdateOrderRequest req) {

        order.updateOrderItems(req.getOrderItemsRequests());

        // TODO: 재고 비교 값 필요
        int stockQuantity = 10000;
        int totalQuantity = 0;
        for (OrderItemsRequest request : req.getOrderItemsRequests()) {
            totalQuantity += request.getStock();
        }
        validateProductStock(stockQuantity, order.getTotalStock());

        order.updateStockAndRequestsDetails(totalQuantity, req.getRequestDetails());

        return order;
    }

    public void validateProductStock(int stockQuantity, int totalStock) {
        if (stockQuantity < totalStock) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
    }

    public Page<Order> searchOrders(int page, SearchDto searchDto) {

        int pageSize =
            (searchDto.getSize() == 10 || searchDto.getSize() == 30 || searchDto.getSize() == 50)
                ? searchDto.getSize() : 10;

        Sort sort =
            searchDto.getDirection().equalsIgnoreCase("asc") ? Sort.by(searchDto.getSortBy())
                .ascending() : Sort.by(searchDto.getSortBy()).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Order> orderPage;

        if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
            orderPage = jpaOrderRepository.findByRequestDetailsContainingAndIsDeletedFalse(searchDto.getKeyword(),
                pageable);
        } else {
            orderPage = jpaOrderRepository.findByIsDeletedFalse(pageable);
        }
        return orderPage;
    }


}
