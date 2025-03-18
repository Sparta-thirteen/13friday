package com.sparta.orderservice.application.service;


import com.sparta.orderservice.application.dto.OrderItemsDto;
import com.sparta.orderservice.domain.model.OrderItems;
import com.sparta.orderservice.domain.service.OrderDomainService;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.infrastructure.repository.JpaOrderRepository;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import com.sparta.orderservice.presentation.response.OrderResponse;
import com.sparta.orderservice.presentation.response.UpdateOrderResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        // TODO : 허브에 물품 재고가 없는 경우는 주문이 실패해야 합니다.
        // TODO : 배송도 같이 생성되어야 합니다.

        jpaOrderRepository.save(order);

        return ResponseEntity.ok("주문 생성 완료");
    }

    // 주문 취소
    @Transactional
    public ResponseEntity<String> cancelOrder(UUID orderId, Long userId) {

        Order order = findOrder(orderId);

        // TODO: deleted_at,deleted_by 에 시간,아이디 추가

        return ResponseEntity.ok("주문 삭제 완료");
    }


    // 주문 수정
    @Transactional
    public UpdateOrderResponse updateOrder(UUID orderId, UpdateOrderRequest req) {

        Order order = findOrder(orderId);

        order = orderDomainService.updateOrder(order, req);

        List<OrderItemsDto> list = getOrderItemsDtoList(order);


        return new UpdateOrderResponse(order.getTotalStock(), order.getRequestDetails(),
            list);

    }

    // 주문 단건 조회
    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) {

        Order order = findOrder(orderId);

        List<OrderItemsDto> list = getOrderItemsDtoList(order);

        return new OrderResponse(order.getSuppliersId(), order.getRecipientsId(),
            order.getDeliveryId(), order.getTotalStock(), order.getRequestDetails(), list);
    }


    // 주문 전체 조회
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // 최신순 정렬
        Page<Order> orderPage = jpaOrderRepository.findAll(pageable);


        return orderPage.map(order -> new OrderResponse(order.getSuppliersId(), order.getRecipientsId(),
            order.getDeliveryId(), order.getTotalStock(), order.getRequestDetails(), list));
    }

    public Order findOrder(UUID orderId) {
        Order order = jpaOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문 아이디가 없습니다."));

        return order;
    }

    public List<OrderItemsDto> getOrderItemsDtoList(Order order) {
        return order.getOrderItems().stream()
            .map(item -> new OrderItemsDto(item.getId(), item.getName(), item.getStock()))
            .toList();
    }
}
