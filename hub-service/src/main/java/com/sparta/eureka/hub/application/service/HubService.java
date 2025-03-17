package com.sparta.eureka.hub.application.service;

import com.sparta.eureka.hub.application.dto.HubDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.domain.repository.HubRepository;
import com.sparta.eureka.hub.infrastructure.mapper.HubMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;
    private final HubMapper hubMapper;

    public HubDto.responseDto createHub(HubDto.createDto request) {
        Hub hub = hubRepository.save(hubMapper.createDtoToHub(request));

        return hubMapper.hubToResponseDto(hub);
    }

    public HubDto.responseDto updateHub(UUID hubId, HubDto.updateDto request) {
        Hub hub = findHub(hubId);

        return null;
    }

    public Hub findHub(UUID hubId) {
        Optional<Hub> hub = hubRepository.findById(hubId);
        return hub.orElseThrow(() ->
                new EntityNotFoundException("Hub with id " + hubId + " not found"));
    }
}
