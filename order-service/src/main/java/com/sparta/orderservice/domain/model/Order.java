package com.sparta.orderservice.domain.model;


import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "p_order")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID suppliersId;
    private UUID recipientsId;
    private UUID deliveryId;
    private int totalStock;
    private String requestDetails;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems = new ArrayList<>();

    public Order(UUID suppliersId, UUID recipientsId, UUID deliveryId, String requestDetails) {
        this.suppliersId = suppliersId;
        this.recipientsId = recipientsId;
        this.deliveryId = deliveryId;
        this.requestDetails = requestDetails;
    }


    public void addOrderItem(OrderItems item) {
        this.orderItems.add(item);
        item.setOrder(this);
        this.totalStock += item.getStock();
    }

    public void updateOrderItems(List<OrderItemsRequest> req, Map<UUID, OrderItems> existsItemsMap) {


        for (OrderItemsRequest request : OrderItemsRequest) {
            for (OrderItems item : this.orderItems) {
                if (item.getProductId().equals(request.getProductId())) {
                    item.updateOrderItem(request.getName(), request.getStock());
                }
            }
        }
    }

    public void updateStockAndRequestsDetails(int totalStock, String requestDetails) {
        this.totalStock = totalStock;
        this.requestDetails = requestDetails;
    }


}


