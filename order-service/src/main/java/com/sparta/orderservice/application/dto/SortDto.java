package com.sparta.orderservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SortDto {

    private int page;
    private int size;
    private String sortBy;
    private String direction;

}
