package com.sparta.company_service.common.infrastructure.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderItemsRequest {

  @NotEmpty
  private UUID productId;
  private String name;
  private int stock;
}
