package com.sparta.company_service.product.application.dto;

import com.sparta.company_service.product.domain.entity.Product;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderResponseDto {

  private UUID productId;
  private UUID hubId;
  private UUID companyId;

  public static ProductOrderResponseDto toDto(Product product) {
    return ProductOrderResponseDto.builder()
        .productId(product.getId())
        .hubId(product.getHubId())
        .companyId(product.getCompanyId())
        .build();
  }
}
