package com.sparta.eureka.hub.application.dto.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

public class HubDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDto {
        @NotBlank
        private String hubName;
        @NotBlank
        private String address;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateDto {
        private String hubName;
        private String address;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUserDto {
        private UUID hubId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private UUID hubId;
        private Long userId;
        private String hubName;
        private String address;
        private BigDecimal lat;
        private BigDecimal lon;
    }

}
