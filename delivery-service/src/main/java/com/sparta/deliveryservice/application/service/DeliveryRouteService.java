package com.sparta.deliveryservice.application.service;


import com.sparta.deliveryservice.application.dto.DeliveryRouteDto;
import com.sparta.deliveryservice.domain.model.DeliveryRoute;
import com.sparta.deliveryservice.domain.model.DeliveryRouteType;
import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.domain.model.SortDto;
import com.sparta.deliveryservice.domain.service.DeliveryRouteDomainService;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRouteRepository;
import com.sparta.deliveryservice.presentation.response.DeliveryRouteResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryRouteService {

    private final JpaDeliveryRouteRepository jpaDeliveryRouteRepository;
    private final DeliveryRouteDomainService deliveryRouteDomainService;
    private final StringRedisTemplate redisTemplate;

//
//    // 배송경로 생성
//    @Transactional
//    public ResponseEntity<String> createDeliveryRoute() {
//
////        1	대전 허브 → 부산 허브	허브 배송 담당자	✅ 생성
////        2	부산 허브 → 수령 업체	업체 배송 담당자	✅ 생성
//
////        0	HUB_WAITING	(null) → 공급허브	X (배송 전)
////        1	HUB_PROGRESS	공급허브 → 수령허브	허브배송
////        2	HUB_DELIVERED	수령허브 도착 상태용	X
////        3	COMPANY_PROGRESS	수령허브 → 수령업체	업체배송
////        4	COMPANY_DELIVERED	수령업체 도착 상태용	X
//
//        // TODO: deliveryService
//        // 요청으로 받아옴: 공급업체허브id,수령업체허브id,배송id,배송주소
//
//        // TODO: 순번주는 로직
//
//        // TODO: shippingmanager-service
//        // 요청: 배송허브id,배송순번
//        // 응답 : 배송업체담당자id
//
//        DeliveryRoute deliveryRoute = DeliveryRoute.testDeliveryRoute(
//            DeliveryRouteType.COMPANY_DELIVERED, 0);
//        jpaDeliveryRouteRepository.save(deliveryRoute);
//        return ResponseEntity.ok("배송경로 생성 완료");
//    }


    // 배송경로생성
    public void createDeliveryRoutes(UUID deliveryId, UUID departureHubId, UUID destinationHubId,
        String shippingAddress) {

        // TODO: deliveryServic 요청으로 받아옴: 공급업체허브id,수령업체허브id,배송id,배송주소

        // TODO: hub-service 에서 받아와야함. -> 응답 : 거리,시간

        // TODO: shippingmanager-service -> null,배송순번 / 허브배송담당자 id

        // 허브 배송 상태
        List<DeliveryRouteType> hubTypes = List.of(
            DeliveryRouteType.HUB_WAITING,
            DeliveryRouteType.HUB_PROGRESS,
            DeliveryRouteType.HUB_DELIVERED
        );

        int deliveryOrder = getNextDeliveryOrder(null, "HUB_MANAGER");
        UUID shippingManagerId = UUID.randomUUID(); // TODO: 실제 서비스 호출로 변경

        DeliveryRouteDto dto = new DeliveryRouteDto(departureHubId, destinationHubId,
            deliveryId, shippingAddress, 1000L, LocalDateTime.now(), 1000L,
            LocalDateTime.now());
        for (DeliveryRouteType type : hubTypes) {
            DeliveryRouteDto routDto = type.createDtoFromBase(dto);
            createDeliveryRouteByType(routDto, shippingManagerId, type, deliveryOrder);
        }

        // TODO: shippingmanager-service -> 출발지허브id,배송순번 / 업체배송담당자 id
        // 업체 배송 상태
        List<DeliveryRouteType> companyTypes = List.of(
            DeliveryRouteType.COMPANY_PROGRESS,
            DeliveryRouteType.COMPANY_DELIVERED
        );

        deliveryOrder = getNextDeliveryOrder(destinationHubId, "COMPANY_MANAGER");
        shippingManagerId = UUID.randomUUID(); // TODO: 실제 서비스 호출로 변경

        for (DeliveryRouteType type : companyTypes) {
            DeliveryRouteDto routDto = type.createDtoFromBase(dto);
            createDeliveryRouteByType(routDto, shippingManagerId, type, deliveryOrder);
        }

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
            route.getDeliveryStatus(), route.getDeliveryOrder());
        return ResponseEntity.ok(response);
    }


    // 특정 배송의 모든 경로 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<DeliveryRouteResponse>> getDeliveryRouteByDeliveryAddress(
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
                route.getDeliveryStatus(), route.getDeliveryOrder())).toList();

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
                route.getDeliveryStatus(), route.getDeliveryOrder())).toList();

        return ResponseEntity.ok(response);
    }

    // 배송경로 검색
    @Transactional(readOnly = true)
    public List<DeliveryRouteResponse> searchDeliveryRoute(SearchDto searchDto) {

        Page<DeliveryRoute> deliveryRoutePage = deliveryRouteDomainService.searchDeliveryRoute(
            searchDto);

        return deliveryRoutePage.getContent().stream()
            .map(route -> new DeliveryRouteResponse(route.getDepartureHubId(),
                route.getDestinationHubId(),
                route.getDeliveryId(), route.getShippingManagerId(), route.getShippingAddress(),
                route.getEstimatedDistance(),
                route.getEstimatedTime(), route.getActualDistance(), route.getActualTime(),
                route.getDeliveryStatus(), route.getDeliveryOrder())).toList();
    }


    private DeliveryRoute findDeliveryRoute(UUID deliveryRouteId) {
        return jpaDeliveryRouteRepository.findById(deliveryRouteId)
            .orElseThrow(() -> new IllegalArgumentException("배송경로 아이디가 없습니다."));
    }


    private void isDeletedDeliveryRoute(DeliveryRoute deliveryRoute) {
        if (deliveryRoute.isDeleted()) {
            throw new IllegalArgumentException("삭제된 배송경로입니다.");
        }
    }

    private void createDeliveryRouteByType(DeliveryRouteDto dto, UUID shippingManagerId,
        DeliveryRouteType type,
        int deliveryOrder) {
        DeliveryRoute deliveryRoute;
        if (type.equals(DeliveryRouteType.HUB_WAITING)) {
            dto.setDeliveryRouteDto(0L, null, 0L, null);
        }

        deliveryRoute = new DeliveryRoute(
            dto.getDepartureHubId(),
            dto.getDestinationHubId(),
            dto.getDeliveryId(),
            shippingManagerId,
            dto.getShippingAddress(),
            dto.getEstimatedDistance(),
            dto.getEstimatedTime(),
            dto.getActualDistance(),
            dto.getActualTime(),
            type,
            deliveryOrder
        );

        jpaDeliveryRouteRepository.save(deliveryRoute);

    }


    private int getNextDeliveryOrder(UUID hubId, String role) {
        String key = generateRedisKey(hubId, role);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Long currentValue = ops.increment(key); // 기존값 없으면 1부터 시작
        int next = (int) ((currentValue - 1) % 10); // 순번은 0부터 시작되도
        log.info("check: "+next);
        return next;
    }


    private String generateRedisKey(UUID hubId, String role) {
        String id = (hubId != null) ? hubId.toString() : "common";

        return String.format("delivery:order:%s:%s", id, role);
    }


}
