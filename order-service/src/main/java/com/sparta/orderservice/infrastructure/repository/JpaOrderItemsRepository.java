package com.sparta.orderservice.infrastructure.repository;

import com.sparta.orderservice.domain.model.OrderItems;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderItemsRepository extends JpaRepository<OrderItems, UUID> {

}
