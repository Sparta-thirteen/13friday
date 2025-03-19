package com.sparta.deliveryservice.application.service;


import com.sparta.deliveryservice.domain.model.BaseEntity;
import com.sparta.deliveryservice.domain.model.Delivery;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRepository;
import com.sparta.deliveryservice.presentation.request.DeliveryRequest;
import com.sparta.deliveryservice.presentation.request.UpdateDeliveryRequest;
import com.sparta.deliveryservice.presentation.response.UpdateDeliveryResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final JpaDeliveryRepository jpaDeliveryRepository;

    @Transactional
    public ResponseEntity<String> createDelivery(DeliveryRequest req) {
        // TODO: id 전부 외부 api로 받아야함.
        Delivery delivery = new Delivery(req.getDestination_hub_id(), req.getDestination_hub_id(),
            req.getShipping_manager_id(), req.getShipping_manager_slack_id(),
            req.getCompany_delivery_manager_id(), req.getShipping_address(),
            req.getDelivery_status());

        jpaDeliveryRepository.save(delivery);
        return ResponseEntity.ok("배송 생성 완료");
    }

    @Transactional
    public ResponseEntity<String> deleteDelivery(UUID deliveryId) {
        Delivery delivery = findDelivery(deliveryId);
        delivery.delete(String.valueOf(deliveryId));
        return ResponseEntity.ok("배송 삭제 완료");
    }

    @Transactional
    public ResponseEntity<String> updateDelivery(UUID deliveryId,
        UpdateDeliveryRequest req) {
        // TODO: id 전부 외부 api로 받아야함.
        Delivery delivery = findDelivery(deliveryId);
        if (delivery.isDeleted()) {
            throw new IllegalArgumentException("삭제된 배송정보입니다.");
        }

        delivery.updateDelivery(req);

        return ResponseEntity.ok("배송 상태 수정 완료 " + req.getDelivery_status());
    }

    private Delivery findDelivery(UUID deliveryId) {
        return jpaDeliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new IllegalArgumentException("배송 아이디가 없습니다."));

    }

}
