package com.sparta.deliveryservice.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryRequestDto {
    private UUID hubId;
    private int deliveryOrder;
}
