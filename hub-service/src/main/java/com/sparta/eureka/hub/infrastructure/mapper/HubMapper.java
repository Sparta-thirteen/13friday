package com.sparta.eureka.hub.infrastructure.mapper;

import com.sparta.eureka.hub.application.dto.hub.HubDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HubMapper {
    default Hub createDtoToHub(HubDto.CreateDto request){
        return Hub.create(
                request.getHubName(),
                request.getAddress()
        );
    }

    Hub updateDtoToHub(HubDto.UpdateDto request);

    default HubDto.ResponseDto hubToResponseDto(Hub hub) {
        HubDto.ResponseDto response =  new HubDto.ResponseDto();
        response.setHubId(hub.getHubId());
        response.setHubName(hub.getHubName());
        response.setAddress(hub.getAddress());
        response.setLat(hub.getLat());
        response.setLon(hub.getLon());

        return response;
    }

}
