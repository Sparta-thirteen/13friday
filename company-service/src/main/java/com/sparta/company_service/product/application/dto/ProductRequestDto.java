package com.sparta.company_service.product.application.dto;

import com.sparta.company_service.product.domain.entity.Product;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductRequestDto {

  private UUID companyId;
  private String name;

  public Product toEntity(UUID hubId) {
    return Product.builder()
        .companyId(this.companyId)
        .hubId(hubId)
        .name(this.name)
        .build();
  }
}
