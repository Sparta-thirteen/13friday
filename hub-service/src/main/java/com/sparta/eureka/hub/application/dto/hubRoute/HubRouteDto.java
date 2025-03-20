package com.sparta.eureka.hub.application.dto.hubRoute;

import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public class HubRouteDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDto {
        private UUID departHubId;
        private UUID arriveHubId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateDto {
        private UUID departHubId;
        private UUID arriveHubId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private UUID hubRouteId;
        private UUID departHubId;
        private UUID arriveHubId;
        private Long estimatedTime;
        private BigDecimal distance;
    }
}
