package com.sparta.deliveryservice.domain.repository;


import com.sparta.deliveryservice.domain.model.Delivery;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {}


