package com.sparta.deliveryservice.infrastructure.repository;


import com.sparta.deliveryservice.domain.model.Delivery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findById(UUID deliveryId);

    Page<Delivery> findAll( Pageable pageable);
    Page<Delivery> findByIsDeletedFalse( Pageable pageable);
    Page<Delivery> findByShippingAddressContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Delivery>  findByShippingAddressContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
}
