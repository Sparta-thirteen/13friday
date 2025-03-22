package com.sparta.eureka.client.auth.common.client.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubRequestDto {
  private UUID hubId;
  private Long userId;
  private String hubName;
  private String address;
  private BigDecimal lat;
  private BigDecimal lon;
}
