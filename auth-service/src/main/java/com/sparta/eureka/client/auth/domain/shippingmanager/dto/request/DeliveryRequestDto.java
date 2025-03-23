package com.sparta.eureka.client.auth.domain.shippingmanager.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequestDto {
  private UUID hubId;
  private int deliveryOrder;
}
