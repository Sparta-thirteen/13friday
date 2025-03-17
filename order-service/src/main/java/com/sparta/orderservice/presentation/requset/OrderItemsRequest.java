package com.sparta.orderservice.presentation.requset;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class OrderItemsRequest {

    @NotEmpty
    private UUID productId;
    private int productStock;

}
