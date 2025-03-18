package com.sparta.eureka.hub.application.dto;

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
    public static class createDto {
        @NotBlank
        private String hubName;
        @NotBlank
        private String address;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class updateDto {
        @NotBlank
        private String hubName;
        @NotBlank
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class responseDto {
        private UUID hubId;
        private String hubName;
        private String address;
        private BigDecimal lat;
        private BigDecimal lon;
    }

}
