package com.sparta.orderservice.domain.service;

import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.domain.model.OrderItems;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import java.util.ArrayList;
import java.util.List;

public class OrderDomainService {



    public Order createOrder(OrderRequest req) {
        // OrderItem 엔티티 생성 및 저장
        List<OrderItems> itemList = new ArrayList<>();


        // TODO: 재고 비교 값 필요
        int totalStock = 0;
        int stockQuantity = 10000;
        for (OrderItemsRequest request : req.getOrderItemsRequests()) {
            itemList.add(new OrderItems(request.getProductId(),request.getProductStock()));
            totalStock += request.getProductStock();
            validateProductStock(stockQuantity,totalStock);
        }

        // Order 엔티티 생성
        Order order = new Order(req.getSuppliersId(), req.getRecipientsId(), req.getDeliveryId(),
            totalStock, req.getRequestDetails(), itemList);

        return order;
    }

    public void validateProductStock(int stockQuantity,int totalStock) {
        if(stockQuantity < totalStock) throw new IllegalStateException("재고가 부족합니다.");
    }




}
