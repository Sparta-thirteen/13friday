package com.sparta.deliveryservice.presentation.request;



import com.sparta.deliveryservice.domain.model.DeliveryType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRequest {


    @NotBlank
    private UUID departureHubId;
    @NotBlank
    private UUID destinationHubId;
    @NotBlank
    private UUID shippingManagerId;
    @NotBlank
    private UUID shippingManagerSlackId;
    @NotBlank
    private UUID companyDeliveryManagerId;
    @NotBlank
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryStatus;




}
