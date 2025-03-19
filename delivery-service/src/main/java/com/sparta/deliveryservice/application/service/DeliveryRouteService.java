package com.sparta.deliveryservice.application.service;


import com.sparta.deliveryservice.domain.model.DeliveryRoute;
import com.sparta.deliveryservice.domain.model.DeliveryRouteType;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRouteRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryRouteService {

    private final JpaDeliveryRouteRepository jpaDeliveryRouteRepository;


    // 배송경로 생성
    @Transactional
    public ResponseEntity<String> createDeliveryRoute() {
        // TODO : 외부API로 데이터 받기, 모든 배송경로 로직 구성
        DeliveryRoute deliveryRoute = DeliveryRoute.testDeliveryRoute(
            DeliveryRouteType.COMPANY_DELIVERED, 0);
        jpaDeliveryRouteRepository.save(deliveryRoute);
        return ResponseEntity.ok("배송경로 생성 완료");
    }


    // 배송경로 삭제
    @Transactional
    public ResponseEntity<String> deleteDeliveryRoute(UUID deliveryRouteId) {
        DeliveryRoute deliveryRoute = findDeliveryRoute(deliveryRouteId);
        isDeletedDeliveryRoute(deliveryRoute);
        deliveryRoute.delete(String.valueOf(deliveryRouteId));
        return ResponseEntity.ok("배송 삭제 완료");
    }

//    // 배송경로 수정
//    @Transactional
//    public ResponseEntity<String> updateDeliveryRoute(UUID deliveryId,
//        UpdateDeliveryRequest req) {
//        // TODO: id 전부 외부 api로 받아야함.
//        Delivery delivery = findDeliveryRoute(deliveryRouteId);
//        if (delivery.isDeleted()) {
//            throw new IllegalArgumentException("삭제된 배송정보입니다.");
//        }
//
//        delivery.updateDelivery(req);
//
//        return ResponseEntity.ok("배송 상태 수정 완료 " + req.getDelivery_status());
//    }

    private DeliveryRoute findDeliveryRoute(UUID deliveryRouteId) {
        return jpaDeliveryRouteRepository.findById(deliveryRouteId)
            .orElseThrow(() -> new IllegalArgumentException("배송경로 아이디가 없습니다."));
    }


    private void isDeletedDeliveryRoute(DeliveryRoute deliveryRoute) {
        if (deliveryRoute.isDeleted()) {
            throw new IllegalArgumentException("삭제된 배송경로입니다.");
        }
    }

}
