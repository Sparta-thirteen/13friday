package com.sparta.orderservice.domain.repository;


import com.sparta.orderservice.domain.model.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {}


