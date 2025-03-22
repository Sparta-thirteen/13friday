package com.sparta.eureka.hub.application.service;

import com.sparta.eureka.hub.application.dto.hub.HubDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.domain.repository.HubRepository;
import com.sparta.eureka.hub.infrastructure.common.exception.BusinessLogicException;
import com.sparta.eureka.hub.infrastructure.common.exception.ErrorCode;
import com.sparta.eureka.hub.infrastructure.geocoding.Coordinates;
import com.sparta.eureka.hub.infrastructure.geocoding.GeocodingService;
import com.sparta.eureka.hub.infrastructure.mapper.HubMapper;
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

    public HubDto.ResponseDto createHub(String role, HubDto.CreateDto request) {
        if(role.equals("MASTER")) {
            Hub hub = hubRepository.save(hubMapper.createDtoToHub(request));

            return hubMapper.hubToResponseDto(hub);
        } else {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
        }
    }



    @Transactional
    public HubDto.ResponseDto updateHub(String role, UUID hubId, HubDto.UpdateDto request) {
        Hub hub = findHub(hubId);
        if(role.equals("MASTER")) {
            Hub updateHub = hubMapper.updateDtoToHub(request);
            hub.update(updateHub.getHubName(), updateHub.getAddress());

            return hubMapper.hubToResponseDto(hub);
        } else {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
        }
    }

    public Page<HubDto.ResponseDto> getHubs(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Hub> hub = hubRepository.findAllByIsDeletedFalse(pageable);

        return hub.map(hubMapper::hubToResponseDto);
    }

    public HubDto.ResponseDto getHub(UUID hubId) {
        Hub hub = findHub(hubId);
        if(!hub.isDeleted()) {
            return hubMapper.hubToResponseDto(hub);
        } else {
            throw new BusinessLogicException(ErrorCode.HUB_NOT_FOUND);
        }
    }

    public Page<HubDto.ResponseDto> searchHubs(int size,
                                               int page,
                                               String keyword,
                                               boolean isDesc) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Hub> hubs = hubRepository.searchByKeyword(keyword, isDesc, pageable);

        return hubs.map(hubMapper::hubToResponseDto);
    }

    @Transactional
    public HubDto.ResponseDto addHubAuth(String role, UUID hubId, HubDto.UpdateUserDto request) {
        Hub hub = findHub(hubId);
        if(role.equals("MASTER")) {
            hub.updateUserId(request.getHubUserId());

            return hubMapper.hubToResponseDto(hub);
        } else {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    public void deleteHub(String role, UUID hubId) {
        if(role.equals("MASTER")) {
            Hub hub = findHub(hubId);
            hub.delete();
        } else {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
        }
    }

    public Hub findHub(UUID hubId) {
        Optional<Hub> hub = hubRepository.findById(hubId);

        return hub.orElseThrow(() -> new BusinessLogicException(ErrorCode.HUB_NOT_FOUND));
    }

    @Transactional
    public void updateHubCoordinates(String role, UUID hubId) {
        Hub hub = findHub(hubId);
        if(role.equals("MASTER")) {
            Coordinates coordinates = geocodingService.getCoordinatesFromAddress(hub.getAddress());

            hub.latAndLon(
                    BigDecimal.valueOf(coordinates.getLat()),
                    BigDecimal.valueOf(coordinates.getLon())
            );

        } else {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
        }
    }
}
