package com.sparta.slack_service.common.infrastructure.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemsDto {

  private UUID id;
  private String name;
  private int stock;
}
