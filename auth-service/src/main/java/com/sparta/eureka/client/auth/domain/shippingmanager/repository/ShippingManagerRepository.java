package com.sparta.eureka.client.auth.domain.shippingmanager.repository;

import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManager;
import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManagerType;
import com.sparta.eureka.client.auth.domain.shippingmanager.repository.custom.ShippingManagerRepositoryCustom;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingManagerRepository extends JpaRepository<ShippingManager, UUID>,
    ShippingManagerRepositoryCustom {

  @Query("SELECT MAX(s.deliveryOrder) FROM ShippingManager s WHERE s.type = :type")
  Optional<Integer> findMaxDeliveryOrderByType(@Param("type") ShippingManagerType type);
}
