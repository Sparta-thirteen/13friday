package com.sparta.deliveryservice.application.dto;


import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private String departHubName;
        private String arriveHubName;
        private Long estimatedTime;
        private BigDecimal distance;
    }
}
