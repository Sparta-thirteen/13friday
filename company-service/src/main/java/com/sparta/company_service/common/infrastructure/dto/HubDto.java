package com.sparta.company_service.common.infrastructure.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class HubDto {

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
