package com.sparta.orderservice.domain.model;


import com.sparta.orderservice.presentation.requset.OrderRequest;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "p_order")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Id
    private UUID id;
    private String name;
    private String email;

    private UUID suppliersId;
    private UUID recipientsId;
    private UUID deliveryId;
    private int totalStock;

    private String requestDetails;


    public Order(UUID orderId, String name, String email, UUID suppliersId, UUID recipientsId,
        UUID deliveryId,
        String requestDetails, int totalStock) {
        this.id = orderId;
        this.name = name;
        this.email = email;
        this.suppliersId = suppliersId;
        this.recipientsId = recipientsId;
        this.deliveryId = deliveryId;
        this.requestDetails = requestDetails;
        this.totalStock = totalStock;
    }


    public void updateStockAndRequestsDetails(int totalStock, String requestDetails) {
        this.totalStock = totalStock;
        this.requestDetails = requestDetails;
    }

    public void createdByOrder(String userName) {
        this.setCreatedBy(userName);
    }


}


