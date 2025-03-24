package com.sparta.orderservice.infrastructure.repository;

import com.sparta.orderservice.domain.model.OrderItems;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderItemsRepository extends JpaRepository<OrderItems, UUID> {


    List<OrderItems> findAllByOrderId(UUID orderId);

}
