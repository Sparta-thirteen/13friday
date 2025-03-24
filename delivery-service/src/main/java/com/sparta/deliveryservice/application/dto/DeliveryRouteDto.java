package com.sparta.deliveryservice.application.dto;


import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRouteDto {

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
    private LocalDateTime estimatedTime;
    @NotBlank
    private Long actualDistance;
    private LocalDateTime actualTime;

    public DeliveryRouteDto(UUID departureHubId,UUID destinationHubId,UUID deliveryId,String shippingAddress,Long estimatedDistance,LocalDateTime estimatedTime, Long actualDistance,LocalDateTime actualTime) {
        this.departureHubId = departureHubId;
        this.destinationHubId =destinationHubId;
        this.deliveryId =deliveryId;
        this.shippingAddress = shippingAddress;
        this.estimatedDistance =estimatedDistance;
    this.estimatedTime =estimatedTime;
    this.actualDistance = actualDistance;
    this.actualTime =actualTime;
    }

    public void setDeliveryRouteDto(Long estimatedDistance, LocalDateTime estimatedTime, Long actualDistance, LocalDateTime actualTime) {
        this.estimatedDistance = estimatedDistance;
        this.estimatedTime = estimatedTime;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
    }

}
