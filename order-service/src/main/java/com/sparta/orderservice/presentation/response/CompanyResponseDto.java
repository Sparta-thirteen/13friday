package com.sparta.orderservice.presentation.response;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDto {

    private UUID companyId;
    private UUID hubId;
    private Long userId;
    private String name;
    private String address;


}