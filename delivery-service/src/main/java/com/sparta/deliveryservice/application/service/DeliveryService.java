package com.sparta.deliveryservice.application.service;


import com.sparta.deliveryservice.application.dto.DeliveryInfoDto;
import com.sparta.deliveryservice.application.dto.DeliveryRouteDto;
import com.sparta.deliveryservice.application.dto.DeliveryRoutesDto;
import com.sparta.deliveryservice.common.CustomException;
import com.sparta.deliveryservice.common.GlobalExceptionCode;
import com.sparta.deliveryservice.domain.model.Delivery;
import com.sparta.deliveryservice.domain.model.DeliveryRouteType;
import com.sparta.deliveryservice.domain.model.DeliveryType;
import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.domain.model.SortDto;
import com.sparta.deliveryservice.domain.service.DeliveryDomainService;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRepository;
import com.sparta.deliveryservice.presentation.request.DeliveryRequest;
import com.sparta.deliveryservice.presentation.request.UpdateDeliveryRequest;
import com.sparta.deliveryservice.presentation.response.DeliveryCreatedResponse;
import com.sparta.deliveryservice.presentation.response.DeliveryInternalResponse;
import com.sparta.deliveryservice.presentation.response.DeliveryResponse;

import java.util.List;
import java.util.UUID;
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
public class DeliveryService {

    private final JpaDeliveryRepository jpaDeliveryRepository;
    private final DeliveryDomainService deliveryDomainService;
    private final DeliveryRouteService deliveryRouteService;

    // 배송 생성
    @Transactional
    public ResponseEntity<DeliveryCreatedResponse> createDelivery(DeliveryRequest req,
        String role) {

        createRoleCheck(role);

        Delivery delivery = new Delivery(req.getDepartureHubId(), req.getDestinationHubId(),
            req.getRecipientsId(), req.getOrderId(), req.getShippingAddress());
        delivery.createdByDelivery(req.getRecipientsName());
        jpaDeliveryRepository.save(delivery);

        // 배송경로기록 생성
        DeliveryRoutesDto dto = deliveryRouteService.createDeliveryRoutes(delivery.getId(),
            req.getDepartureHubId(), req.getDestinationHubId(), req.getShippingAddress(),req.getRecipientsName());

        delivery.updateDeliveryInfo(dto.getRecipientsSlackId(), dto.getCompanyDeliveryManagerId());

        DeliveryCreatedResponse response = new DeliveryCreatedResponse(delivery.getId());
        return ResponseEntity.ok(response);
    }

    // 배송 삭제
    @Transactional
    public ResponseEntity<String> deleteDelivery(UUID deliveryId, String userId, String role) {
        deleteRoleCheck(role);

        Delivery delivery = findDelivery(deliveryId);
        if (delivery.isDeleted()) {
            throw new IllegalArgumentException("삭제된 배송정보입니다.");
        }
        delivery.delete(String.valueOf(deliveryId));
        return ResponseEntity.ok("배송 삭제 완료");
    }

    // 배송 수정
    @Transactional
    public ResponseEntity<String> updateDelivery(UUID deliveryId,
        UpdateDeliveryRequest req, String userId, String role) {

        updateRoleCheck(role);

        // TODO: id 전부 외부 api로 받아야함.
        Delivery delivery = findDelivery(deliveryId);

        isDeletedDelivery(delivery);

        delivery.updateDelivery(req);

        return ResponseEntity.ok("배송 상태 수정 완료 " + req.getDelivery_status());
    }

    // 배송 단건 조회
    @Transactional(readOnly = true)
    public DeliveryResponse getDelivery(UUID deliveryId) {

        Delivery delivery = findDelivery(deliveryId);

        isDeletedDelivery(delivery);

        // TODO: id 전부 외부 api로 받아야함.
        return new DeliveryResponse(delivery.getDepartureHubId(), delivery.getDestinationHubId(),
            delivery.getRecipientsId(), delivery.getRecipientsSlackId(),
            delivery.getCompanyDeliveryManagerId(), delivery.getOrderId(),
            delivery.getShippingAddress(),
            delivery.getDeliveryStatus());
    }


    // 주문에 따른 배송조회
    @Transactional(readOnly = true)
    public List<DeliveryResponse> getDeliveryByOrder(UUID orderId, SortDto sortDto) {
        Sort sort = sortDto.getDirection().equalsIgnoreCase("asc")
            ? Sort.by(sortDto.getSortBy()).ascending()
            : Sort.by(sortDto.getSortBy()).descending();
        int pageSize =
            (sortDto.getSize() == 10 || sortDto.getSize() == 30 || sortDto.getSize() == 50)
                ? sortDto.getSize() : 10;

        Pageable pageable = PageRequest.of(pageSize, sortDto.getSize(), sort);

        Page<Delivery> deliveryPage = jpaDeliveryRepository.findByOrderIdAndIsDeletedFalse(orderId,
            pageable);

        return deliveryPage.getContent().stream()
            .map(delivery -> new DeliveryResponse(delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                delivery.getRecipientsId(), delivery.getRecipientsSlackId(),
                delivery.getCompanyDeliveryManagerId(), delivery.getOrderId(),
                delivery.getShippingAddress(),
                delivery.getDeliveryStatus())).toList();
    }

    // 배송 전체 조회
    @Transactional(readOnly = true)
    public List<DeliveryResponse> getDeliveries(SortDto sortDto) {
        Sort sort = sortDto.getDirection().equalsIgnoreCase("asc")
            ? Sort.by(sortDto.getSortBy()).ascending()
            : Sort.by(sortDto.getSortBy()).descending();

        int pageSize =
            (sortDto.getSize() == 10 || sortDto.getSize() == 30 || sortDto.getSize() == 50)
                ? sortDto.getSize() : 10;

        Pageable pageable = PageRequest.of(pageSize, sortDto.getSize(), sort);

        Page<Delivery> deliveryPage = jpaDeliveryRepository.findByIsDeletedFalse(pageable);

        return deliveryPage.getContent().stream()
            .map(delivery -> new DeliveryResponse(delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                delivery.getRecipientsId(), delivery.getRecipientsSlackId(),
                delivery.getCompanyDeliveryManagerId(), delivery.getOrderId(),
                delivery.getShippingAddress(),
                delivery.getDeliveryStatus())).toList();
    }

    // 배송 검색
    @Transactional(readOnly = true)
    public List<DeliveryResponse> searchDeliveries(SearchDto searchDto) {
        Page<Delivery> deliveryPage = deliveryDomainService.searchDeliveries(searchDto);

        return deliveryPage.getContent().stream()
            .map(delivery -> new DeliveryResponse(delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                delivery.getRecipientsId(), delivery.getRecipientsSlackId(),
                delivery.getCompanyDeliveryManagerId(), delivery.getOrderId(),
                delivery.getShippingAddress(),
                delivery.getDeliveryStatus())).toList();
    }


    private Delivery findDelivery(UUID deliveryId) {
        return jpaDeliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new IllegalArgumentException("배송 아이디가 없습니다."));
    }

    private void isDeletedDelivery(Delivery delivery) {
        if (delivery.isDeleted()) {
            throw new IllegalArgumentException("삭제된 배송정보입니다.");
        }
    }

//    private Delivery initTestDelivery(UUID orderId, String shippingAddress,
//        DeliveryType deliveryStatus) {
//        UUID departureHubId = UUID.randomUUID();
//        UUID destinationHubId = UUID.randomUUID();
//        UUID recipientsId = UUID.randomUUID();
//        UUID recipientsSlackId = UUID.randomUUID();
//        UUID companyDeliveryManagerId = UUID.randomUUID();
//
//        return new Delivery(departureHubId, destinationHubId, recipientsId, recipientsSlackId,
//            companyDeliveryManagerId, orderId, shippingAddress, deliveryStatus);
//    }

    private void createRoleCheck(String role) {
        if (!role.equals("MASTER")) {
            throw new CustomException(GlobalExceptionCode.INVALID_ROLE);
        }
    }


    private void updateRoleCheck(String role) {
        if (role.equals("COMPANYMANAGER")) {
            throw new CustomException(GlobalExceptionCode.INVALID_ROLE);
        }

    }

    private void deleteRoleCheck(String role) {
        if (role.equals("SHIPPINGMANAGER")) {
            throw new CustomException(GlobalExceptionCode.INVALID_ROLE);
        }
        if (role.equals("COMPANYMANAGER")) {
            throw new CustomException(GlobalExceptionCode.INVALID_ROLE);
        }
    }

}
