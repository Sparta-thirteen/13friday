package com.sparta.deliveryservice.application.service;


import com.sparta.deliveryservice.domain.model.BaseEntity;
import com.sparta.deliveryservice.domain.model.Delivery;
import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.domain.service.DeliveryDomainService;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRepository;
import com.sparta.deliveryservice.presentation.request.DeliveryRequest;
import com.sparta.deliveryservice.presentation.request.UpdateDeliveryRequest;
import com.sparta.deliveryservice.presentation.response.DeliveryResponse;
import com.sparta.deliveryservice.presentation.response.UpdateDeliveryResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final JpaDeliveryRepository jpaDeliveryRepository;
    private final DeliveryDomainService deliveryDomainService;

    // 배송 생성
    @Transactional
    public ResponseEntity<String> createDelivery(DeliveryRequest req) {
        // TODO: id 전부 외부 api로 받아야함.
        Delivery delivery = new Delivery(req.getDepartureHubId(), req.getDestinationHubId(),
            req.getShippingManagerId(), req.getShippingManagerSlackId(),
            req.getCompanyDeliveryManagerId(), req.getShippingAddress(),
            req.getDeliveryStatus());

        jpaDeliveryRepository.save(delivery);
        return ResponseEntity.ok("배송 생성 완료");
    }

    // 배송 삭제
    @Transactional
    public ResponseEntity<String> deleteDelivery(UUID deliveryId) {
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
        UpdateDeliveryRequest req) {
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
            delivery.getShippingManagerId(), delivery.getShippingManagerSlackId(),
            delivery.getCompanyDeliveryManagerId(), delivery.getShippingAddress(),
            delivery.getDeliveryStatus());
    }


    // 배송 전체 조회
    @Transactional(readOnly = true)
    public List<DeliveryResponse> getDeliveries(int page, int size, String sortBy,
        String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Delivery> deliveryPage = jpaDeliveryRepository.findByIsDeletedFalse(pageable);

        return deliveryPage.getContent().stream()
            .map(delivery -> new DeliveryResponse(delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                delivery.getShippingManagerId(), delivery.getShippingManagerSlackId(),
                delivery.getCompanyDeliveryManagerId(), delivery.getShippingAddress(),
                delivery.getDeliveryStatus())).toList();
    }

    // 배송 검색
    @Transactional(readOnly = true)
    public List<DeliveryResponse> searchDeliveries(int page, SearchDto searchDto) {
        // TODO : 리팩토링
        Page<Delivery> deliberyPage = deliveryDomainService.searchDeliveries(page, searchDto);

        return deliberyPage.getContent().stream()
            .map(delivery -> new DeliveryResponse(delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                delivery.getShippingManagerId(), delivery.getShippingManagerSlackId(),
                delivery.getCompanyDeliveryManagerId(), delivery.getShippingAddress(),
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

}
