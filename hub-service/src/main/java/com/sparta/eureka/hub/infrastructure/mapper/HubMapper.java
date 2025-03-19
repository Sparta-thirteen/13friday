package com.sparta.eureka.hub.infrastructure.mapper;

import com.sparta.eureka.hub.application.dto.HubDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HubMapper {
    default Hub createDtoToHub(HubDto.createDto request){
        return Hub.create(
                request.getHubName(),
                request.getAddress()
        );
    }
    Hub updateDtoToHub(HubDto.updateDto request);
//    default HubDto.responseDto hubToResponseDto(Hub hub) {
//        HubDto.responseDto response =  new HubDto.responseDto();
//        response.setHubId(hub.getHubId());
//        response.setHubName(hub.getHubName());
//        response.setAddress(hub.getAddress());
//        response.setLat(hub.getLat());
//        response.setLon(hub.getLon());
//
//        return response;
//    }
    HubDto.responseDto hubToResponseDto(Hub hub);
}
