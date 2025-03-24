package com.sparta.deliveryservice.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingManagerResponseDto {
    private UUID shippingManagerId;
    private String slackId;
    private Long userId;

}