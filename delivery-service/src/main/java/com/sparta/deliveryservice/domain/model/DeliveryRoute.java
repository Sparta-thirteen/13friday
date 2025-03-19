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
    private UUID deliveryManagerId;

    @Column(nullable = false)
    private UUID companyDeliveryManagerId;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private Long estimatedDistance;

    @Column(nullable = false)
    private LocalDateTime estimatedTime;

    @Column(nullable = false)
    private Long actualDistance;

    @Column(nullable = false)
    private LocalDateTime actualTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryRouteType deliveryStatus;

    @Column(nullable = false)
    private int sequence;

    public DeliveryRoute(UUID departureHubId, UUID destinationHubId, UUID deliveryId,
        UUID deliveryManagerId, UUID companyDeliveryManagerId, String shippingAddress,
        Long estimatedDistance, LocalDateTime estimatedTime, Long actualDistance,
        LocalDateTime actualTime, DeliveryRouteType deliveryStatus, int sequence) {
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.deliveryId = deliveryId;
        this.deliveryManagerId = deliveryManagerId;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
        this.shippingAddress = shippingAddress;
        this.estimatedDistance = estimatedDistance;
        this.estimatedTime = estimatedTime;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        this.deliveryStatus = deliveryStatus;
        this.sequence = sequence;
    }



    public static DeliveryRoute testDeliveryRoute(DeliveryRouteType deliveryStatus, int sequence) {
       return new DeliveryRoute(UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),"강남구 123",150L,LocalDateTime.now().plusDays(2),148L,LocalDateTime.now().plusDays(2).plusHours(1),deliveryStatus,sequence);
    }

}
