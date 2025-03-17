package com.sparta.eureka.hub.infrastructure.mapper;

import com.sparta.eureka.hub.application.dto.HubDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HubMapper {
    default Hub createDtoToHub(HubDto.createDto request){
        return Hub.create(
                request.getHubName(),
                request.getHubAddress(),
                request.getLat(),
                request.getLon()
        );
    }
    Hub updateDtoToHub(HubDto.updateDto request);
    HubDto.responseDto hubToResponseDto(Hub hub);
}
