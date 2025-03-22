package com.sparta.eureka.client.auth.domain.shippingmanager.repository.custom;

import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManager;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingManagerRepositoryCustom {
  Page<ShippingManager> findShippingManagers(
      UUID shippingManagerId,
      UUID hubId,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable);
}
