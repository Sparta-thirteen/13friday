package com.sparta.orderservice.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Table(name="p_orderitems")
@Getter
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID productId;
    private int productStock;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    public OrderItems(UUID productId, int productStock) {
        this.productId =productId;
        this.productStock =productStock;
    }

    public void setOrder(Order order) {
        this.order =order;
        order.getOrderItems().add(this);

    }
}


