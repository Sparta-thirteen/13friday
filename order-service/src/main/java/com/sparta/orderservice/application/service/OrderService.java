package com.sparta.orderservice.application.service;


import com.sparta.orderservice.application.dto.DeliveryInfoDto;
import com.sparta.orderservice.application.dto.OrderItemsDto;
import com.sparta.orderservice.application.dto.SortDto;
import com.sparta.orderservice.common.CustomException;
import com.sparta.orderservice.common.GlobalExceptionCode;
import com.sparta.orderservice.domain.model.OrderItems;
import com.sparta.orderservice.domain.model.SearchDto;
import com.sparta.orderservice.domain.service.OrderDomainService;
import com.sparta.orderservice.infrastructure.client.DeliveryClient;
import com.sparta.orderservice.infrastructure.repository.JpaOrderItemsRepository;
import com.sparta.orderservice.presentation.requset.DeliveryRequest;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import com.sparta.orderservice.presentation.requset.OrderRequest;
import com.sparta.orderservice.domain.model.Order;
import com.sparta.orderservice.infrastructure.repository.JpaOrderRepository;
import com.sparta.orderservice.presentation.requset.UpdateOrderRequest;
import com.sparta.orderservice.presentation.response.DeliveryCreatedResponse;
import com.sparta.orderservice.presentation.response.DeliveryInternalResponse;
import com.sparta.orderservice.presentation.response.OrderInternalResponse;
import com.sparta.orderservice.presentation.response.OrderResponse;
import com.sparta.orderservice.presentation.response.UpdateOrderResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderDomainService orderDomainService;
    private final DeliveryClient deliveryClient;
    private final JpaOrderItemsRepository jpaOrderItemsRepository;

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

        //  주문 아이템 저장
        List<OrderItems> items = orderRequest.getOrderItemsRequests().stream()
            .map(req -> new OrderItems(req.getProductId(), req.getName(), req.getStock(), orderId))
            .toList();

        jpaOrderItemsRepository.saveAll(items);

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

        // 1. 기존 아이템 불러오기
        List<OrderItems> existingItems = jpaOrderItemsRepository.findAllByOrderId(orderId);
        Map<UUID, OrderItems> itemMap = existingItems.stream()
            .collect(Collectors.toMap(OrderItems::getId, i -> i));

        // 2. 요청된 아이템 수량/이름 업데이트
        for (OrderItemsRequest updateReq : req.getOrderItemsRequests()) {
            if (itemMap.containsKey(updateReq.getProductId())) {
                itemMap.get(updateReq.getProductId())
                    .updateOrderItem(updateReq.getName(), updateReq.getStock());
            }
        }

        log.info(existingItems.get(0).getName()+" "+existingItems.get(0).getStock());


        // 3. 도메인 서비스에게 주문 정보 수정 위임
        order = orderDomainService.updateOrder(order, req);

        List<OrderItemsDto> list = getOrderItemsDtoList(order.getId());

        return new UpdateOrderResponse(order.getTotalStock(), order.getRequestDetails(),
            list);

    }

    // 주문 단건 조회
    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) {

        Order order = findOrder(orderId);

        List<OrderItemsDto> list = getOrderItemsDtoList(orderId);

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
            (sortDto.getSize() == 10 || sortDto.getSize() == 30 || sortDto.getSize() == 50)
                ? sortDto.getSize() : 10;

        Pageable pageable = PageRequest.of(sortDto.getPage(), pageSize, sort);

        Page<Order> orderPage = jpaOrderRepository.findByIsDeletedFalse(pageable);

        return orderPage.getContent().stream()
            .map(order -> new OrderResponse(order.getSuppliersId(), order.getRecipientsId(),
                order.getDeliveryId(), order.getTotalStock(), order.getRequestDetails(),
                getOrderItemsDtoList(order.getId()))).toList();
    }

    // TODO : 리팩토링
    // 주문 검색
    @Transactional(readOnly = true)
    public List<OrderResponse> searchOrders(int page, SearchDto searchDto) {

        Page<Order> orderPage = orderDomainService.searchOrders(page, searchDto);

        return orderPage.getContent().stream()
            .map(order -> new OrderResponse(order.getSuppliersId(), order.getRecipientsId(),
                order.getDeliveryId(), order.getTotalStock(), order.getRequestDetails(),
                getOrderItemsDtoList(order.getId()))).toList();
    }

    public Order findOrder(UUID orderId) {
        Order order = jpaOrderRepository.findById(orderId)
            .orElseThrow(() -> new CustomException(GlobalExceptionCode.INVALID_ORDER));

        return order;
    }

    public List<OrderItemsDto> getOrderItemsDtoList(UUID orderId) {
        return jpaOrderItemsRepository.findAllByOrderId(orderId)
            .stream()
            .map(item -> new OrderItemsDto(item.getId(), item.getName(), item.getStock()))
            .toList();
    }

    public OrderInternalResponse getOrderInternal(UUID orderId) {
        Order order = findOrder(orderId);
        List<OrderItemsDto> orderItemsDtoList = getOrderItemsDtoList(orderId);
        DeliveryInfoDto dto = new DeliveryInfoDto(orderId,order.getDeliveryId());
        log.info(dto.getOrderId().toString());
        DeliveryInternalResponse response  = deliveryClient.getDeliveryInfo(dto);


        return new OrderInternalResponse(order.getName(),order.getEmail(),orderItemsDtoList,order.getRequestDetails(),response.getDepartHubId(),response.getArriveHubId(),response.getHubUserId());
    }
}
