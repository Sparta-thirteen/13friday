package com.sparta.eureka.hub.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class HubDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class createDto {
        @NotBlank
        private String hubName;
        @NotBlank
        private String hubAddress;
        @NotBlank
        private BigDecimal lat;
        @NotBlank
        private BigDecimal lon;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class updateDto {
        @NotBlank
        private String hubName;
        @NotBlank
        private String hubAddress;
        @NotBlank
        private BigDecimal lat;
        @NotBlank
        private BigDecimal lon;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class responseDto {
        private String hubName;
        private String hubAddress;
        private BigDecimal lat;
        private BigDecimal lon;
    }

}
