package com.sparta.deliveryservice.presentation.request;


import jakarta.persistence.Column;
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
    private String recipientsName;
    private String role;





}
