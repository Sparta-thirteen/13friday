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
    private UUID recipientsId;
    @Column(nullable = false)
    private String recipientsSlackId;
    @Column(nullable = false)
    private UUID companyDeliveryManagerId;
    @Column(nullable = false)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryType deliveryStatus;
    @Column(nullable = false)
    private UUID orderId;

    public Delivery(UUID departureHubId, UUID destinationHubId, UUID recipientsId, String recipientsSlackId, UUID companyDeliveryManagerId,UUID orderId, String shippingAddress, DeliveryType deliveryStatus) {
        this.departureHubId =departureHubId;
        this.destinationHubId =destinationHubId;
        this.recipientsId = recipientsId;
        this.recipientsSlackId =recipientsSlackId;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
        this.orderId = orderId;
        this.shippingAddress =shippingAddress;
        this.deliveryStatus =deliveryStatus;
    }

    public Delivery(UUID departureHubId, UUID destinationHubId, UUID recipientsId,UUID orderId, String shippingAddress) {
        this.departureHubId =departureHubId;
        this.destinationHubId =destinationHubId;
        this.recipientsId = recipientsId;
        this.orderId = orderId;
        this.shippingAddress =shippingAddress;
        this.recipientsSlackId = "slack";
        this.companyDeliveryManagerId = UUID.randomUUID();
        this.deliveryStatus = DeliveryType.DELIVERED;
    }

    public void updateDelivery(UpdateDeliveryRequest req) {
        this.deliveryStatus = req.getDelivery_status();
    }

    public void updateDeliveryInfo(String recipientsSlackId,UUID companyDeliveryManagerId) {
        this.recipientsSlackId = recipientsSlackId;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
    }

    public void createdByDelivery(String userName) {
        this.setCreatedBy(userName);
    }

}


