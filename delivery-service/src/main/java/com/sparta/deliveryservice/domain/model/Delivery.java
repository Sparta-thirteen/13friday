package com.sparta.deliveryservice.domain.model;


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
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID departure_hub_id;
    @Column(nullable = false)
    private UUID destination_hub_id;
    @Column(nullable = false)
    private UUID shipping_manager_id;
    @Column(nullable = false)
    private UUID shipping_manager_slack_id;
    @Column(nullable = false)
    private UUID company_delivery_manager_id;
    @Column(nullable = false)
    private String shipping_address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryType delivery_status;


}


