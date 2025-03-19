package com.sparta.orderservice.presentation.response;


import com.sparta.orderservice.application.dto.OrderItemsDto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class OrderResponse {

    @NotBlank
    private UUID suppliersId;
    @NotBlank
    private UUID recipientsId;
    @NotBlank
    private UUID deliveryId;
    @NotBlank
    private int totalStock;
    private String requestDetails;

    private List<OrderItemsDto> orderItems;

}
