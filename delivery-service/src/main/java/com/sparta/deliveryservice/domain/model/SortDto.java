package com.sparta.deliveryservice.domain.model;


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
