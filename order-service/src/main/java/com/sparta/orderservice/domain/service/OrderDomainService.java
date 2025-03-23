package com.sparta.orderservice.domain.service;

import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.domain.model.SearchDto;
import com.sparta.orderservice.infrastructure.repository.JpaOrderRepository;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;

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

    public Order createOrder(UUID orderId,OrderRequest req,UUID deliveryId) {

        int totalStock = req.getOrderItemsRequests().stream()
            .mapToInt(OrderItemsRequest::getStock)
            .sum();

        // Order 엔티티 생성
        Order order = new Order(orderId,req.getSuppliersId(), req.getRecipientsId(), deliveryId,totalStock,
            req.getRequestDetails());

        return order;
    }

    public Order updateOrder(Order order, UpdateOrderRequest req) {

        int totalStock = req.getOrderItemsRequests().stream()
            .mapToInt(OrderItemsRequest::getStock)
            .sum();


        order.updateStockAndRequestsDetails(totalStock, req.getRequestDetails());

        return order;
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
