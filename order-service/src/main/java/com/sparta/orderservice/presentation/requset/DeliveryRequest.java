package com.sparta.orderservice.presentation.requset;


import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRequest {
    @NotBlank
    private UUID orderId;
    @NotBlank
    private UUID recipientsId;
    @NotBlank
    private UUID departureHubId;
    @NotBlank
    private UUID destinationHubId;
    @NotBlank
    private String shippingAddress;




}
