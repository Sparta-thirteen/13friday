package com.sparta.orderservice.application.service;


import com.sparta.orderservice.application.dto.OrderItemsDto;
import com.sparta.orderservice.application.dto.SortDto;
import com.sparta.orderservice.common.CustomException;
import com.sparta.orderservice.common.GlobalExceptionCode;
import com.sparta.orderservice.domain.model.SearchDto;
import com.sparta.orderservice.domain.service.OrderDomainService;
import com.sparta.orderservice.infrastructure.client.DeliveryClient;
import com.sparta.orderservice.presentation.advice.GlobalExceptionHandler;
import com.sparta.orderservice.presentation.requset.DeliveryRequest;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.infrastructure.repository.JpaOrderRepository;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import com.sparta.orderservice.presentation.response.DeliveryCreatedResponse;
import com.sparta.orderservice.presentation.response.OrderResponse;
import com.sparta.orderservice.presentation.response.UpdateOrderResponse;
import jakarta.validation.constraints.NotEmpty;
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
    private final DeliveryClient deliveryClient;

    // 주문 생성
    @Transactional
    public ResponseEntity<String> createOrder(OrderRequest orderRequest) {

        // TODO: item-service
        // 요청: 공급업체이름, 리스트로 (상품아이디, 상품이름, 재고수량)
        // 응답 : 공급업체id 공급업체허브id

        String supplierName = "공급업체";

        // TODO: company-service
        // 요청: 수령업체이름
        // 응답 : 수령업체id 수령업체허브id,수령업체주소
        String recipientsName = "수령업체";


        UUID orderId = UUID.randomUUID();

        // TODO: delivery-service
        // 요청: 공급업체허브id,수령업체허브id,수령업체id,배송주소,주문id
        // 응답 : 배송id
        DeliveryRequest deliveryRequest = new DeliveryRequest(orderId, UUID.randomUUID(),
            "서울특별시 강남구 역삼동 858");

        DeliveryCreatedResponse response = deliveryClient.createDelivery(deliveryRequest);


        Order order = orderDomainService.createOrder(orderId, orderRequest, response.getId());
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

        // TODO: 주문 수정하기 위한 stock 필요

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
    public List<OrderResponse> getOrders(SortDto sortDto) {

        Sort sort = sortDto.getDirection().equalsIgnoreCase("asc")
            ? Sort.by(sortDto.getSortBy()).ascending()
            : Sort.by(sortDto.getSortBy()).descending();


        int pageSize =
            (sortDto.getSize() == 10 || sortDto.getSize()== 30 || sortDto.getSize()== 50)
                ? sortDto.getSize(): 10;


        Pageable pageable = PageRequest.of(pageSize, sortDto.getSize(), sort);

        Page<Order> orderPage = jpaOrderRepository.findByIsDeletedFalse(pageable);

        return orderPage.getContent().stream()
            .map(order -> new OrderResponse(order.getSuppliersId(), order.getRecipientsId(),
                order.getDeliveryId(), order.getTotalStock(), order.getRequestDetails(),
                getOrderItemsDtoList(order))).toList();
    }

    // TODO : 리팩토링
    // 주문 검색
    @Transactional(readOnly = true)
    public List<OrderResponse> searchOrders(int page, SearchDto searchDto) {

        Page<Order> orderPage = orderDomainService.searchOrders(page, searchDto);

        return orderPage.getContent().stream()
            .map(order -> new OrderResponse(order.getSuppliersId(), order.getRecipientsId(),
                order.getDeliveryId(), order.getTotalStock(), order.getRequestDetails(),
                getOrderItemsDtoList(order))).toList();
    }

    public Order findOrder(UUID orderId) {
        Order order = jpaOrderRepository.findById(orderId)
            .orElseThrow(() -> new CustomException(GlobalExceptionCode.INVALID_ORDER));

        return order;
    }

    public List<OrderItemsDto> getOrderItemsDtoList(Order order) {
        return order.getOrderItems().stream()
            .map(item -> new OrderItemsDto(item.getId(), item.getName(), item.getStock()))
            .toList();
    }
}
