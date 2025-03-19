package com.sparta.deliveryservice.domain.model;


import com.sparta.deliveryservice.presentation.request.UpdateDeliveryRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "p_delivery")
@Getter
@RequiredArgsConstructor
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID departureHubId;
    @Column(nullable = false)
    private UUID destinationHubId;
    @Column(nullable = false)
    private UUID shippingManagerId;
    @Column(nullable = false)
    private UUID shippingManagerSlackId;
    @Column(nullable = false)
    private UUID companyDeliveryManagerId;
    @Column(nullable = false)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryType deliveryStatus;


    public Delivery(UUID departureHubId, UUID destinationHubId, UUID shippingManagerId, UUID shippingManagerSlackId, UUID companyDeliveryManagerId, String shippingAddress, DeliveryType deliveryStatus) {
        this.departureHubId =departureHubId;
        this.destinationHubId =destinationHubId;
        this.shippingManagerId = shippingManagerId;
        this.shippingManagerSlackId =shippingManagerSlackId;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
        this.shippingAddress =shippingAddress;
        this.deliveryStatus =deliveryStatus;
    }


    public void updateDelivery(UpdateDeliveryRequest req) {
        this.deliveryStatus = req.getDelivery_status();
    }
}


