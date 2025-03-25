package com.sparta.eureka.client.auth.domain.shippingmanager.dto.response;

import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManager;
import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManagerType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadResponseDto {
  private UUID id;
  private Long userId;
  private String slackId;
  private String hubId;
  private ShippingManagerType type;
  private LocalDateTime createdAt;

  public static ReadResponseDto fromEntity(ShippingManager shippingManager) {
    return ReadResponseDto.builder()
        .id(shippingManager.getId())
        .userId(shippingManager.getUserId())
        .slackId(shippingManager.getSlackId())
        .hubId(shippingManager.getHubId() != null ? shippingManager.getHubId().toString() : null)
        .type(shippingManager.getType())
        .createdAt(shippingManager.getCreatedAt())
        .build();
  }
}
