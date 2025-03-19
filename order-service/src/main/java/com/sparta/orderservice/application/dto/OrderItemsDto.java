package com.sparta.orderservice.application.dto;

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
