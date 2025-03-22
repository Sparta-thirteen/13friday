package com.sparta.eureka.client.auth.domain.shippingmanager.dto.response;

import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManager;
import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManagerType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingManagerResponseDto {
  private UUID shippingManagerId;
  private Long userId;
  private String slackId;
  private UUID hubId;
  private ShippingManagerType type;
  private int deliveryOrder;

  public static ShippingManagerResponseDto fromEntity(ShippingManager shippingManager){
    return new ShippingManagerResponseDto(
        shippingManager.getId(),
        shippingManager.getUserId(),
        shippingManager.getSlackId(),
        shippingManager.getHubId(),
        shippingManager.getType(),
        shippingManager.getDeliveryOrder()
    );
  }
}
