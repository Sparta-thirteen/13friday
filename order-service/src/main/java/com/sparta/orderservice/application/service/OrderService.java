package com.sparta.orderservice.application.service;


import com.sparta.orderservice.domain.service.OrderDomainService;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.infrastructure.repository.JpaOrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderDomainService orderDomainService;

    // 주문 생성
    @Transactional
    public ResponseEntity<String> createOrder(OrderRequest req) {

        Order order = orderDomainService.createOrder(req);

        jpaOrderRepository.save(order);

        return ResponseEntity.ok("주문 생성 완료");
    }

    // 주문 삭제
    @Transactional
    public ResponseEntity<String> deleteOrder(UUID orderId) {

        Order order = jpaOrderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문 아이디가 없습니다."));

        jpaOrderRepository.delete(order);


        return ResponseEntity.ok("주문 삭제 완료");
    }
}

