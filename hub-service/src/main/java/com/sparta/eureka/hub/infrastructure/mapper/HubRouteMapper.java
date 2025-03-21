package com.sparta.eureka.hub.infrastructure.mapper;

import com.sparta.eureka.hub.application.dto.hubRoute.HubRouteDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.domain.entity.HubRoute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HubRouteMapper {
    default HubRoute createDtoToHubRoute(Hub departHub, Hub arriveHub) {

        return HubRoute.create(departHub, arriveHub);
    }

    HubRoute updateDtoToHubRoute(HubRouteDto.UpdateDto request);

    default HubRouteDto.ResponseDto hubRouteToResponseDto(HubRoute hubRoute) {
        return HubRouteDto.ResponseDto.builder()
                .hubRouteId(hubRoute.getHubRouteId())
                .departHubId(hubRoute.getDepartHub().getHubId())
                .arriveHubId(hubRoute.getArriveHub().getHubId())
                .departHubName(hubRoute.getDepartHub().getHubName())
                .arriveHubName(hubRoute.getArriveHub().getHubName())
                .estimatedTime(hubRoute.getEstimatedTime())
                .distance(hubRoute.getDistance())
                .build();

    }
}
