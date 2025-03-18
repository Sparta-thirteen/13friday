package com.sparta.orderservice.domain.service;

import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.domain.model.OrderItems;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderDomainService {


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

    public Order updateOrder(Order order,UpdateOrderRequest req) {


        Map<UUID, OrderItems> existsItemsMap = new HashMap<>();
        for (OrderItems item : order.getOrderItems()) {
            existsItemsMap.put(item.getId(), item);
        }
        order.updateOrderItems(req.getOrderItemsRequests(),existsItemsMap);

        // TODO: 재고 비교 값 필요
        int stockQuantity = 10000;
        int totalQuantity = 0;
        for (OrderItemsRequest request : req.getOrderItemsRequests()) {
            OrderItems orderItems = existsItemsMap.get(request.getProductId());
            totalQuantity += request.getStock();
        }
        validateProductStock(stockQuantity, order.getTotalStock());

        order.updateStockAndRequestsDetails(totalQuantity,req.getRequestDetails());

        return order;
    }

    public void validateProductStock(int stockQuantity, int totalStock) {
        if (stockQuantity < totalStock) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
    }


}
