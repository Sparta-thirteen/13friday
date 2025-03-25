package com.sparta.orderservice.presentation.response;


import com.sparta.orderservice.application.dto.CompanyType;
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
    private CompanyType type;
    private String name;
    private String address;


}