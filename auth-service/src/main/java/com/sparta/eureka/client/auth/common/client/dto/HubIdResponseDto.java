package com.sparta.eureka.client.auth.common.client.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HubIdResponseDto {
  private UUID hubId;
}
