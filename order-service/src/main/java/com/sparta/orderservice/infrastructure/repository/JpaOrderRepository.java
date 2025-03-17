package com.sparta.orderservice.infrastructure.repository;

import com.sparta.orderservice.domain.model.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {


    Optional<Order> findById(UUID id);
}
