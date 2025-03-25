package com.sparta.deliveryservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "p_deliveryroute")
@Getter
@RequiredArgsConstructor
public class DeliveryRoute extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID departureHubId;

    @Column(nullable = false)
    private UUID destinationHubId;

    @Column(nullable = false)
    private UUID deliveryId;

    @Column(nullable = false)
    private UUID shippingManagerId;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private Long estimatedDistance;

    private Long estimatedTime;

    @Column(nullable = false)
    private Long actualDistance;

    private Long actualTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryRouteType deliveryStatus;

    @Column(nullable = false)
    private int deliveryOrder;

    @Column(nullable = false)
    private Long hubDeliveryUserId;

    public DeliveryRoute(UUID departureHubId, UUID destinationHubId, UUID deliveryId,
        UUID shippingManagerId, String shippingAddress,
        Long estimatedDistance, Long estimatedTime, Long actualDistance,
        Long actualTime, DeliveryRouteType deliveryStatus, int deliveryOrder,
        Long hubDeliveryUserId) {
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.deliveryId = deliveryId;
        this.shippingManagerId = shippingManagerId;
        this.shippingAddress = shippingAddress;
        this.estimatedDistance = estimatedDistance;
        this.estimatedTime = estimatedTime;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        this.deliveryStatus = deliveryStatus;
        this.deliveryOrder = deliveryOrder;
        this.hubDeliveryUserId = hubDeliveryUserId;
    }
    public void createdByDeliveryRoute(String userName) {
        this.setCreatedBy(userName);
    }

}
