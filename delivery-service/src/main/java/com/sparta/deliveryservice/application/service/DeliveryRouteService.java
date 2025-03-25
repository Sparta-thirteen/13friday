package com.sparta.deliveryservice.application.service;


import com.sparta.deliveryservice.application.dto.DeliveryInfoDto;
import com.sparta.deliveryservice.application.dto.DeliveryRequestDto;
import com.sparta.deliveryservice.application.dto.DeliveryRouteDto;
import com.sparta.deliveryservice.application.dto.DeliveryRoutesDto;
import com.sparta.deliveryservice.application.dto.DeliveryRoutesHubIdDto;
import com.sparta.deliveryservice.application.dto.HubRouteDto;
import com.sparta.deliveryservice.application.dto.HubRouteDto.CreateDto;
import com.sparta.deliveryservice.application.dto.ShippingManagerResponseDto;
import com.sparta.deliveryservice.common.CustomException;
import com.sparta.deliveryservice.common.GlobalExceptionCode;
import com.sparta.deliveryservice.domain.model.Delivery;
import com.sparta.deliveryservice.domain.model.DeliveryRoute;
import com.sparta.deliveryservice.domain.model.DeliveryRouteType;
import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.domain.model.SortDto;
import com.sparta.deliveryservice.domain.service.DeliveryRouteDomainService;
import com.sparta.deliveryservice.infrastructure.client.HubClient;
import com.sparta.deliveryservice.infrastructure.client.ShippingManagerClient;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRouteRepository;
import com.sparta.deliveryservice.presentation.response.DeliveryInternalResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryRouteService {

    private final JpaDeliveryRouteRepository jpaDeliveryRouteRepository;
    private final DeliveryRouteDomainService deliveryRouteDomainService;
    private final StringRedisTemplate redisTemplate;
    private final HubClient hubClient;
    private final ShippingManagerClient shippingManagerClient;


    // 배송경로생성
    public DeliveryRoutesDto createDeliveryRoutes(UUID deliveryId, UUID departureHubId,
        UUID destinationHubId,
        String shippingAddress,String recipientsName) {

        // 허브 서비스 통신
        HubRouteDto.CreateDto createDto = new CreateDto(departureHubId, destinationHubId);
        String role = "MASTER";
        HubRouteDto.ResponseDto responseDto = hubClient.createHubRoute(role, createDto);

        DeliveryRouteDto dto = new DeliveryRouteDto(departureHubId, destinationHubId,
            deliveryId, shippingAddress, responseDto.getDistance().longValue(),
            responseDto.getEstimatedTime(), responseDto.getDistance().longValue(),
            responseDto.getEstimatedTime());

        // TODO: shippingmanager-service -> null,배송순번 / 허브배송담당자 id
        // 허브 배송 상태
        List<DeliveryRouteType> hubTypes = List.of(
            DeliveryRouteType.HUB_WAITING,
            DeliveryRouteType.HUB_PROGRESS,
            DeliveryRouteType.HUB_DELIVERED
        );

        int deliveryOrder = getNextDeliveryOrder(null, "HUB_MANAGER");
//        UUID shippingManagerId = UUID.randomUUID(); // TODO: 실제 서비스 호출로 변경

        DeliveryRequestDto deliveryRequestDto = new DeliveryRequestDto(departureHubId,
            deliveryOrder);
        ShippingManagerResponseDto shippingManagerDto = shippingManagerClient.getSearchShippingManagers(
            deliveryRequestDto);

        for (DeliveryRouteType type : hubTypes) {
            DeliveryRouteDto routDto = type.createDtoFromBase(dto);
            createDeliveryRouteByType(routDto, shippingManagerDto.getShippingManagerId(), type,
                deliveryOrder, shippingManagerDto.getUserId(), recipientsName);
        }


        // TODO: shippingmanager-service -> 출발지허브id,배송순번 / 업체배송담당자 id
        // 업체 배송 상태
        List<DeliveryRouteType> companyTypes = List.of(
            DeliveryRouteType.COMPANY_PROGRESS,
            DeliveryRouteType.COMPANY_DELIVERED
        );

        deliveryOrder = getNextDeliveryOrder(destinationHubId, "COMPANY_MANAGER");
        shippingManagerDto = shippingManagerClient.getSearchShippingManagers(deliveryRequestDto);

        for (DeliveryRouteType type : companyTypes) {
            DeliveryRouteDto routDto = type.createDtoFromBase(dto);
            createDeliveryRouteByType(routDto, shippingManagerDto.getShippingManagerId(), type,
                deliveryOrder, shippingManagerDto.getUserId(), recipientsName);
        }

        return new DeliveryRoutesDto(shippingManagerDto.getSlackId(),
            shippingManagerDto.getShippingManagerId());
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
        int deliveryOrder, Long hubDeliveryUserId,String recipientsName) {
        DeliveryRoute deliveryRoute;
//        if (type.equals(DeliveryRouteType.HUB_WAITING)) {
//            dto.setDeliveryRouteDto(0L, null, 0L, null);
//        }

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
            deliveryOrder,
            hubDeliveryUserId
        );
        deliveryRoute.createdByDeliveryRoute(recipientsName);
        jpaDeliveryRouteRepository.save(deliveryRoute);
    }

    private int getNextDeliveryOrder(UUID hubId, String role) {
        String key = generateRedisKey(hubId, role);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Long currentValue = ops.increment(key);
        int next = (int) ((currentValue - 1) % 10);
        return next;
    }

    public String generateRedisKey(UUID hubId, String role) {
        String id = (hubId != null) ? hubId.toString() : "common";
        return String.format("delivery:order:%s:%s", id, role);
    }

    public DeliveryRoute findDeliveryRoute(DeliveryInfoDto dto) {
        return jpaDeliveryRouteRepository
            .findByDeliveryIdAndDeliveryStatus(dto.getDeliveryId(), DeliveryRouteType.HUB_WAITING)
            .orElseThrow(() -> new IllegalArgumentException("해당 조건에 맞는 배송경로가 없습니다."));
    }

    @Transactional(readOnly = true)
    public DeliveryInternalResponse getDeliveryInfo(DeliveryInfoDto dto) {

        DeliveryRoute deliveryRoute = findDeliveryRoute(dto);

        return new DeliveryInternalResponse(deliveryRoute.getDepartureHubId(),
            deliveryRoute.getDestinationHubId(), deliveryRoute.getHubDeliveryUserId()
        );
    }


}
