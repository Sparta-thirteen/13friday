package com.sparta.deliveryservice.application.dto;

import com.sparta.deliveryservice.domain.model.ShippingManagerType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingManagerResponseDto {
    private UUID shippingManagerId;
    private Long userId;
    private String slackId;
    private UUID hubId;
    private ShippingManagerType type;
    private int deliveryOrder;





}


