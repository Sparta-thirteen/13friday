package com.sparta.eureka.hub.infrastructure.mapper;

import com.sparta.eureka.hub.application.dto.hubRoute.HubRouteDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.domain.entity.HubRoute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HubRouteMapper {
    default HubRoute createDtoToHubRoute(HubRouteDto.CreateDto request) {
        Hub departHub = Hub.builder()
                .hubId(request.getDepartHubId())
                .build();
        Hub arriveHub = Hub.builder()
                .hubId(request.getArriveHubId())
                .build();

        return HubRoute.create(departHub, arriveHub);
    }

    HubRoute updateDtoToHubRoute(HubRouteDto.UpdateDto request);

    default HubRouteDto.ResponseDto hubRouteToResponseDto(HubRoute hubRoute) {
        return HubRouteDto.ResponseDto.builder()
                .hubRouteId(hubRoute.getHubRouteId())
                .departHubId(hubRoute.getDepartHub().getHubId())
                .arriveHubId(hubRoute.getArriveHub().getHubId())
                .estimatedTime(hubRoute.getEstimatedTime())
                .distance(hubRoute.getDistance())
                .build();

    }
}
