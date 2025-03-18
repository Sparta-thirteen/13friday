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
public class ProductResponseDto {

  private UUID productId;
  private UUID companyId;
  private UUID hubId;
  private String name;
  private Integer stock;

  public static ProductResponseDto toDto(Product product) {
    return ProductResponseDto.builder()
        .productId(product.getId())
        .companyId(product.getCompanyId())
        .hubId(product.getHubId())
        .name(product.getName())
        .stock(product.getStock())
        .build();
  }
}
