package com.sparta.deliveryservice.infrastructure.repository;

import com.sparta.deliveryservice.domain.model.Delivery;
import com.sparta.deliveryservice.domain.model.DeliveryRoute;
import com.sparta.deliveryservice.domain.model.DeliveryRouteType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {

    Optional<DeliveryRoute> findById(UUID deliveryRouteId);
    Page<DeliveryRoute> findAll( Pageable pageable);
    Page<DeliveryRoute> findByIsDeletedFalse( Pageable pageable);
    Page<DeliveryRoute> findByDeliveryIdAndIsDeletedFalse(UUID deliveryId,Pageable pageable);
    Page<DeliveryRoute>  findByShippingAddressAndIsDeletedFalse(String keyword, Pageable pageable);
    Page<DeliveryRoute>  findByShippingAddressContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
    Optional<DeliveryRoute> findByDeliveryIdAndDeliveryStatus(UUID deliveryId, DeliveryRouteType deliveryStatus);


}
