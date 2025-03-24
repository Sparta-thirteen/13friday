package com.sparta.deliveryservice.presentation.response;


import com.sparta.deliveryservice.domain.model.DeliveryRouteType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRouteResponse {


    @NotBlank
    private UUID departureHubId;
    @NotBlank
    private UUID destinationHubId;
    @NotBlank
    private UUID deliveryId;
    @NotBlank
    private UUID shippingManagerId;

    @NotBlank
    private String shippingAddress;
    @NotBlank
    private Long estimatedDistance;
    @NotBlank
    private Long estimatedTime;
    @NotBlank
    private Long actualDistance;
    @NotBlank
    private Long actualTime;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private DeliveryRouteType deliveryStatus;
    @NotBlank
    private int sequence;


}
