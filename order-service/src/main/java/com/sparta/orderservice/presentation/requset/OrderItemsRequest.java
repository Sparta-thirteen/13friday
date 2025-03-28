package com.sparta.orderservice.presentation.requset;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class OrderItemsRequest {

    @NotEmpty
    private UUID productId;

    private String name;

    private int stock;

    public void updateOrderItem(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }
}
