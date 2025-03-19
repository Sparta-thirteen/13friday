package com.sparta.deliveryservice.application.service;


import com.sparta.deliveryservice.domain.model.DeliveryRoute;
import com.sparta.deliveryservice.domain.model.DeliveryRouteType;
import com.sparta.deliveryservice.domain.model.SortDto;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRouteRepository;
import com.sparta.deliveryservice.presentation.response.DeliveryRouteResponse;
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
        DeliveryRoute route = findDeliveryRoute(deliveryRouteId);
        isDeletedDeliveryRoute(route);
        route.delete(String.valueOf(deliveryRouteId));
        return ResponseEntity.ok("배송 삭제 완료");
    }

    // 배송기록 단건 조회
    @Transactional(readOnly = true)
    public ResponseEntity<DeliveryRouteResponse> getDeliveryRoute(UUID deliveryRouteId) {
        DeliveryRoute route = findDeliveryRoute(deliveryRouteId);
        isDeletedDeliveryRoute(route);
        DeliveryRouteResponse response = new DeliveryRouteResponse(route.getDepartureHubId(),
            route.getDestinationHubId(),
            route.getDeliveryId(), route.getShippingManagerId(), route.getShippingAddress(),
            route.getEstimatedDistance(),
            route.getEstimatedTime(), route.getActualDistance(), route.getActualTime(),
            route.getDeliveryStatus(), route.getSequence());
        return ResponseEntity.ok(response);
    }


    // 특정 배송의 모든 경로 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<DeliveryRouteResponse>> getDeliveryRouteByDelivery(
        UUID deliveryRouteId,
        SortDto sortDto) {
        Sort sort = sortDto.getDirection().equalsIgnoreCase("asc")
            ? Sort.by(sortDto.getSortBy()).ascending()
            : Sort.by(sortDto.getSortBy()).descending();

        Pageable pageable = PageRequest.of(sortDto.getPage(), sortDto.getSize(), sort);

        Page<DeliveryRoute> page = jpaDeliveryRouteRepository.findByDeliveryIdAndIsDeletedFalse(
            deliveryRouteId,
            pageable);

        List<DeliveryRouteResponse> response = page.getContent().stream()
            .map(route -> new DeliveryRouteResponse(route.getDepartureHubId(),
                route.getDestinationHubId(),
                route.getDeliveryId(), route.getShippingManagerId(), route.getShippingAddress(),
                route.getEstimatedDistance(),
                route.getEstimatedTime(), route.getActualDistance(), route.getActualTime(),
                route.getDeliveryStatus(), route.getSequence())).toList();

        return ResponseEntity.ok(response);
    }

    // 모든 배송 경로 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<DeliveryRouteResponse>> getAllDeliveryRoute(SortDto sortDto) {
        Sort sort = sortDto.getDirection().equalsIgnoreCase("asc")
            ? Sort.by(sortDto.getSortBy()).ascending()
            : Sort.by(sortDto.getSortBy()).descending();

        Pageable pageable = PageRequest.of(sortDto.getPage(), sortDto.getSize(), sort);

        Page<DeliveryRoute> page = jpaDeliveryRouteRepository.findByIsDeletedFalse(
            pageable);

        List<DeliveryRouteResponse> response = page.getContent().stream()
            .map(route -> new DeliveryRouteResponse(route.getDepartureHubId(),
                route.getDestinationHubId(),
                route.getDeliveryId(), route.getShippingManagerId(), route.getShippingAddress(),
                route.getEstimatedDistance(),
                route.getEstimatedTime(), route.getActualDistance(), route.getActualTime(),
                route.getDeliveryStatus(), route.getSequence())).toList();

        return ResponseEntity.ok(response);
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
