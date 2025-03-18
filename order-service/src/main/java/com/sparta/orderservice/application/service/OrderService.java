package com.sparta.orderservice.application.service;


import com.sparta.orderservice.domain.service.OrderDomainService;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.infrastructure.repository.JpaOrderRepository;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import com.sparta.orderservice.presentation.response.UpdateOrderResponse;
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

    // 주문 취소
    @Transactional
    public ResponseEntity<String> cancelOrder(UUID orderId, Long userId) {

        Order order = jpaOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문 아이디가 없습니다."));

        // TODO: deleted_at,deleted_by 에 시간,아이디 추가

        return ResponseEntity.ok("주문 삭제 완료");
    }


    // 주문 수정
    @Transactional
    public UpdateOrderResponse updateOrder(UUID orderId, UpdateOrderRequest req) {

        Order order = jpaOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문 아이디가 없습니다."));

        order = orderDomainService.updateOrder(order,req);

        System.out.println(order.getTotalStock());

        return new UpdateOrderResponse(order.getTotalStock(), order.getRequestDetails(),
            order.getOrderItems());

    }

}
