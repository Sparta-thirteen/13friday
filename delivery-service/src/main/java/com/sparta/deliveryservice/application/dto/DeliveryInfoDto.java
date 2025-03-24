package com.sparta.deliveryservice.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryInfoDto {
    private UUID orderId;
    private UUID deliveryId;

}
