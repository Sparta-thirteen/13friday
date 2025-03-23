package com.sparta.eureka.hub.application.service;

import com.sparta.eureka.hub.application.dto.hubRoute.HubRouteDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.domain.entity.HubRoute;
import com.sparta.eureka.hub.domain.repository.HubRepository;
import com.sparta.eureka.hub.domain.repository.HubRouteRepository;
import com.sparta.eureka.hub.infrastructure.client.openRouteService.OpenRouteServiceClient;
import com.sparta.eureka.hub.infrastructure.client.openRouteService.OpenRouteServiceResponse;
import com.sparta.eureka.hub.infrastructure.common.exception.BusinessLogicException;
import com.sparta.eureka.hub.infrastructure.common.exception.ErrorCode;
import com.sparta.eureka.hub.infrastructure.mapper.HubRouteMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteService {
    private final OpenRouteServiceClient openRouteServiceClient;
    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final HubRouteMapper hubRouteMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${open-route-api-key}")
    private String apiKey;

    @Transactional
    public HubRouteDto.ResponseDto createHubRoute(String role, HubRouteDto.CreateDto request) {
        checkMasterRole(role);
        String cacheKey = "hubRoute:" + request.getDepartHubId() + ":" + request.getArriveHubId();

        HubRouteDto.ResponseDto cachedRoute = (HubRouteDto.ResponseDto) redisTemplate.opsForValue().get(cacheKey);
        if (cachedRoute != null) {
            return cachedRoute;
        }

        Hub departHub = findHub(request.getDepartHubId());
        Hub arriveHub = findHub(request.getArriveHubId());

        Optional<HubRoute> existRoute = hubRouteRepository
                .findByDepartHub_HubIdAndArriveHub_HubId(request.getDepartHubId(), request.getArriveHubId());
        HubRoute hubRoute;

        hubRoute = existRoute.orElseGet(() -> {
            HubRoute newHubRoute = hubRouteMapper.createDtoToHubRoute(departHub, arriveHub);
            return hubRouteRepository.save(newHubRoute);
        });

        calculateRouteData(departHub.getHubId(), arriveHub.getHubId(), hubRoute);

        HubRouteDto.ResponseDto response = hubRouteMapper.hubRouteToResponseDto(hubRoute);
        redisTemplate.opsForValue().set(cacheKey, response, Duration.ofHours(1));

        return response;
    }


    @Transactional
    public HubRouteDto.ResponseDto updateHubRoute(String role, UUID hubRouteId, HubRouteDto.UpdateDto request) {
        checkMasterRole(role);
        HubRoute hubRoute = findHubRoute(hubRouteId);

        Hub departHub = findHub(request.getDepartHubId());
        Hub arriveHub = findHub(request.getArriveHubId());
        HubRoute updateHubRoute = hubRouteMapper.updateDtoToHubRoute(request);
        System.out.println("update hub route: " + departHub.getHubId());

        updateHubRoute.update(departHub, arriveHub);
        hubRoute.update(departHub, arriveHub);

        calculateRouteData(
                departHub.getHubId(),
                arriveHub.getHubId(),
                hubRoute);

        return hubRouteMapper.hubRouteToResponseDto(hubRoute);
    }

    public Page<HubRouteDto.ResponseDto> getHubRoutes(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<HubRoute> hubRoutes = hubRouteRepository.findAllByIsDeletedIsFalse(pageable);

        return hubRoutes.map(hubRouteMapper::hubRouteToResponseDto);
    }

    public HubRouteDto.ResponseDto getHubRoute(UUID hubRouteId) {
        HubRoute hubRoute = findHubRoute(hubRouteId);
        if(!hubRoute.isDeleted()) {
            return hubRouteMapper.hubRouteToResponseDto(hubRoute);
        } else {
            throw new BusinessLogicException(ErrorCode.HUB_ROUTE_NOT_FOUND);
        }
    }

    public Page<HubRouteDto.ResponseDto> searchHubRoutes(int page,
                                                         int size,
                                                         String keyword,
                                                         boolean isDesc) {
        Pageable pageable = PageRequest.of(page - 1, size);

       Page<HubRoute> hubRoutes =  hubRouteRepository.searchHubRoutes(keyword, isDesc, pageable);

        return hubRoutes.map(hubRouteMapper::hubRouteToResponseDto);
    }

    @Transactional
    public void deleteHubRoute(String userId, String role, UUID hubRouteId) {
        checkMasterRole(role);
        HubRoute hubRoute = findHubRoute(hubRouteId);
        hubRoute.delete(Long.parseLong(userId));

    }

    public OpenRouteServiceResponse calculateDistance(UUID departHubId, UUID arriveHubId) {
        Hub departHub = findHub(departHubId);
        Hub arriveHub = findHub(arriveHubId);

        String start = departHub.getLon() + "," + departHub.getLat();
        String end = arriveHub.getLon() + "," + arriveHub.getLat();

        return openRouteServiceClient.getRoute(apiKey, start, end);
    }

    public void calculateRouteData(UUID departHubId, UUID arriveHubId, HubRoute hubRoute) {
        OpenRouteServiceResponse response = calculateDistance(departHubId, arriveHubId);

        OpenRouteServiceResponse.Feature.Properties.Summary summary = response.getFeatures().get(0).getProperties().getSummary();

        double distance = summary.getDistance() / 1000;
        long duration = (long) (summary.getDuration() / 60);

        hubRoute.setEstimatedTimeAndDistance(duration, BigDecimal.valueOf(distance));
    }

    public Hub findHub(UUID hubId) {
        Optional<Hub> hub = hubRepository.findById(hubId);
        return hub.orElseThrow(() ->
                new BusinessLogicException(ErrorCode.HUB_NOT_FOUND));
    }

    public HubRoute findHubRoute(UUID hubRouteId) {
        Optional<HubRoute> hubRoute = hubRouteRepository.findById(hubRouteId);

        return hubRoute.orElseThrow(() -> new BusinessLogicException(ErrorCode.HUB_ROUTE_NOT_FOUND));
    }

    public void checkMasterRole(String role) {
        if (!role.equals("MASTER")) {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
        }
    }
}
