package com.sparta.deliveryservice.infrastructure.repository;


import com.sparta.deliveryservice.domain.model.Delivery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findById(UUID deliveryId);
    Page<Delivery> findByOrderIdAndIsDeletedFalse(UUID orderId,Pageable pageable);
    Page<Delivery> findByIsDeletedFalse( Pageable pageable);
    Page<Delivery>  findByShippingAddressContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
}
