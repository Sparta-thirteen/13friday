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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteService {
    private final OpenRouteServiceClient openRouteServiceClient;
    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final HubRouteMapper hubRouteMapper;
    @Value("${open-route-api-key}")
    private String apiKey;

    @Transactional
    public HubRouteDto.ResponseDto createHubRoute(HubRouteDto.CreateDto request) {
        Hub departHub = findHub(request.getDepartHubId());
        Hub arriveHub = findHub(request.getArriveHubId());

        HubRoute hubRoute = hubRouteRepository.save(hubRouteMapper.createDtoToHubRoute(departHub, arriveHub));
        calculateRouteData(request.getDepartHubId(), request.getArriveHubId(), hubRoute);

        return hubRouteMapper.hubRouteToResponseDto(hubRoute);
    }

    @Transactional
    public HubRouteDto.ResponseDto updateHubRoute(UUID hubRouteId, HubRouteDto.UpdateDto request) {
        HubRoute hubRoute = findHubRoute(hubRouteId);
        HubRoute updateHubRoute = hubRouteMapper.updateDtoToHubRoute(request);

        calculateRouteData(request.getDepartHubId(), request.getArriveHubId(), updateHubRoute);

        hubRoute.update(updateHubRoute);

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
    public void deleteHubRoute(UUID hubRouteId) {
        HubRoute hubRoute = findHubRoute(hubRouteId);
        hubRoute.delete();
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
}
