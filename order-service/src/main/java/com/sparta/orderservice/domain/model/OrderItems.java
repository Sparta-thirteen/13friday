package com.sparta.orderservice.domain.model;


import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Table(name="p_orderitems")
@Getter
public class OrderItems extends BaseEntity {
    @Id
    private UUID id;

    private String name;
    private int stock;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    public OrderItems(UUID id,String name, int stock) {
        this.id =id;
        this.name = name;
        this.stock =stock;
    }

    public void setOrder(Order order) {
        this.order =order;
        order.getOrderItems().add(this);

    }

    public void updateOrderItem(String name,int stock){
        this.name = name;
        this.stock = stock;
    }


//    public void cancel(User user) {
//        this.orderStatus = OrderStatus.CANCELLED;
//        this.cancelledAt = LocalDateTime.now();
//        this.cancelledBy = user.getEmail();
//    }
}


