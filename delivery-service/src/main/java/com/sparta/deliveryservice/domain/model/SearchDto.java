package com.sparta.deliveryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchDto {
    private int page;
    private String keyword;
    private String sortBy;
    private String direction;
    private int size;



    public SearchDto() {
        this.sortBy = "createdAt";
        this.direction = "desc";
        this.size = 10;
    }
}