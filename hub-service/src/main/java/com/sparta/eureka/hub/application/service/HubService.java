package com.sparta.eureka.hub.application.service;

import com.sparta.eureka.hub.application.dto.HubDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.domain.repository.HubRepository;
import com.sparta.eureka.hub.infrastructure.geocoding.Coordinates;
import com.sparta.eureka.hub.infrastructure.geocoding.GeocodingService;
import com.sparta.eureka.hub.infrastructure.mapper.HubMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;
    private final HubMapper hubMapper;
    private final GeocodingService geocodingService;

    public HubDto.responseDto createHub(HubDto.createDto request) {
        Hub hub = hubRepository.save(hubMapper.createDtoToHub(request));

        return hubMapper.hubToResponseDto(hub);
    }



    @Transactional
    public HubDto.responseDto updateHub(UUID hubId, HubDto.updateDto request) {
        Hub hub = findHub(hubId);
        Hub updatedHub = hubMapper.updateDtoToHub(request);

        hub.update(updatedHub.getHubName(), updatedHub.getAddress(), updatedHub.getLat(), updatedHub.getLon());

        return hubMapper.hubToResponseDto(hub);
    }

    public Page<HubDto.responseDto> getHubs(int  size, int page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Hub> hub = hubRepository.findAll(pageable);

        return hub.map(hubMapper::hubToResponseDto);
    }

    public HubDto.responseDto getHub(UUID hubId) {
        Hub hub = findHub(hubId);

        return hubMapper.hubToResponseDto(hub);
    }

    @Transactional
    public void deleteHub(UUID hubId) {
        Hub hub = findHub(hubId);
        hub.delete();
    }

    public Hub findHub(UUID hubId) {
        Optional<Hub> hub = hubRepository.findById(hubId);
        return hub.orElseThrow(() ->
                new EntityNotFoundException("Hub with id " + hubId + " not found"));
    }

    public void updateHubCoordinates(UUID hubId) {
        Hub hub = findHub(hubId);

        Coordinates coordinates = geocodingService.getCoordinatesFromAddress(hub.getAddress());

        hub.latAndLon(
                BigDecimal.valueOf(coordinates.getLat()),
                BigDecimal.valueOf(coordinates.getLon())
        );

        hubRepository.save(hub);
    }



}
