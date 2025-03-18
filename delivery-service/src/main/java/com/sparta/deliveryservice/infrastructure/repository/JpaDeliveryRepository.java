package com.sparta.deliveryservice.infrastructure.repository;


import com.sparta.deliveryservice.domain.model.Delivery;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {

}
