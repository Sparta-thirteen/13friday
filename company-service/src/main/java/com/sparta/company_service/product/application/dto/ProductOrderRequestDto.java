package com.sparta.company_service.product.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductOrderRequestDto {

  private List<UUID> productIdList;
}
