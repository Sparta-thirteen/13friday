package com.sparta.deliveryservice.presentation.response;

import com.sparta.deliveryservice.domain.model.DeliveryType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class DeliveryResponse {

    @NotBlank
    private UUID departureHubId;
    @NotBlank
    private UUID destinationHubId;
    @NotBlank
    private UUID shippingManagerId;
    @NotBlank
    private String shippingManagerSlackId;
    @NotBlank
    private UUID companyDeliveryManagerId;
    @NotBlank
    private UUID orderId;
    @NotBlank
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private DeliveryType deliveryStatus;

}
