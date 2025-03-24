package com.sparta.orderservice.presentation.requset;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductOrderRequestDto {

    private List<UUID> productIdList;
}
