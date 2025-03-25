package com.sparta.orderservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchDto {
    private String keyword;
    private String sortBy = "createdAt";
    private String direction = "desc";
    private int size = 10;
}

