package com.sparta.eureka.client.auth.domain.shippingmanager.dto.request;

import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManagerType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateShippingManagerRequestDto {
  private Long userId;
  private String slackId;
  private UUID hubId;
  public ShippingManagerType getShippingManagerType() {
    return (hubId != null) ? ShippingManagerType.COMPANYSHIPPING : ShippingManagerType.HUBSHIPPING;
  }
}
