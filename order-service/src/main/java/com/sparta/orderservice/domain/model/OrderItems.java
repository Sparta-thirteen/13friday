package com.sparta.orderservice.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID productId;
    private int productStock;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItems(UUID productId, int productStock) {
        this.productId =productId;
        this.productStock =productStock;
    }
}


