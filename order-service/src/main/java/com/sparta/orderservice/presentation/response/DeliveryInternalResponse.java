package com.sparta.orderservice.presentation.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryInternalResponse {
    private UUID departHubId;
    private UUID arriveHubId;
    private Long hubUserId;
}
