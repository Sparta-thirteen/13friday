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

  @Query("SELECT MAX(s.deliveryOrder) FROM ShippingManager s WHERE s.hubId = :hubId")
  Optional<Integer> findMaxDeliveryOrderByHubId(@Param("hubId") UUID hubId);

  @Query("SELECT COALESCE(MAX(s.deliveryOrder), 0) FROM ShippingManager s WHERE s.hubId IS NULL")
  Optional<Integer> findMaxDeliveryOrderByHubIdIsNull();

  @Query("SELECT MAX(s.deliveryOrder) FROM ShippingManager s WHERE s.hubId IS NULL")
  Optional<Integer> findMaxDeliveryOrderForHubShipping();

  // 특정 hubId와 deliveryOrder를 가진 ShippingManager 찾기
  Optional<ShippingManager> findByHubIdAndDeliveryOrder(UUID hubId, int deliveryOrder);

  // hubId가 NULL인 ShippingManager 중 특정 deliveryOrder를 가진 ShippingManager 찾기
  Optional<ShippingManager> findByHubIdIsNullAndDeliveryOrder(int deliveryOrder);


}