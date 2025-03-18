package com.sparta.orderservice.infrastructure.repository;

import com.sparta.orderservice.domain.model.Order;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {


    Optional<Order> findById(UUID id);
    Page<Order> findAll( Pageable pageable);
}
