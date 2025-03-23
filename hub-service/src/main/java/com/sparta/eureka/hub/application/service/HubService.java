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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;
    private final HubMapper hubMapper;
    private final GeocodingService geocodingService;
    private final RedisTemplate<String, Object> redisTemplate;

    public HubDto.ResponseDto createHub(String role, HubDto.CreateDto request) {
        checkMasterRole(role);
        Hub hub = hubRepository.save(hubMapper.createDtoToHub(request));

        return hubMapper.hubToResponseDto(hub);
    }
    
    @Transactional
    public HubDto.ResponseDto updateHub(String role, UUID hubId, HubDto.UpdateDto request) {
        checkMasterRole(role);
        Hub hub = findHub(hubId);
        Hub updateHub = hubMapper.updateDtoToHub(request);
        hub.update(updateHub.getHubName(), updateHub.getAddress());

        return hubMapper.hubToResponseDto(hub);
    }

    public Page<HubDto.ResponseDto> getHubs(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Hub> hub = hubRepository.findAllByIsDeletedFalse(pageable);

        return hub.map(hubMapper::hubToResponseDto);
    }

    public HubDto.ResponseDto getHub(UUID hubId) {
        String cacheKey = "hub_" + hubId;

        HubDto.ResponseDto cachedHub = (HubDto.ResponseDto) redisTemplate.opsForValue().get(cacheKey);

        if (cachedHub != null) {
            return cachedHub;
        }
        Hub hub = findHub(hubId);
        if(!hub.isDeleted()) {
            HubDto.ResponseDto response = hubMapper.hubToResponseDto(hub);
            redisTemplate.opsForValue().set(cacheKey, response, Duration.ofMinutes(10));
            return response;
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
    public HubDto.ResponseDto addHubAuth(String role, Long userId, HubDto.UpdateUserDto request) {
        checkMasterRole(role);
        Hub hub = findHub(request.getHubId());
        hub.updateUserId(userId);

        return hubMapper.hubToResponseDto(hub);
       
    }

    public HubDto.ResponseDto getHubByManager(String userId) {
        long managerId = Long.parseLong(userId);
        Hub hub = hubRepository.findByHubUserId(managerId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MANAGER_NOT_FOUND));

        return hubMapper.hubToResponseDto(hub);
    }

    @Transactional
    public void deleteHub(String userId, String role, UUID hubId) {
        checkMasterRole(role);
        Hub hub = findHub(hubId);
        hub.delete(Long.parseLong(userId));
    }

    public Hub findHub(UUID hubId) {
        Optional<Hub> hub = hubRepository.findById(hubId);

        return hub.orElseThrow(() -> new BusinessLogicException(ErrorCode.HUB_NOT_FOUND));
    }

    @Transactional
    public void updateHubCoordinates(String role, UUID hubId) {
        checkMasterRole(role);
        Hub hub = findHub(hubId);

        Coordinates coordinates = geocodingService.getCoordinatesFromAddress(hub.getAddress());

        hub.latAndLon(
                BigDecimal.valueOf(coordinates.getLat()),
                BigDecimal.valueOf(coordinates.getLon())
        );
    }

    public void checkMasterRole(String role) {
        if (!role.equals("MASTER")) {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
        }
    }
}
