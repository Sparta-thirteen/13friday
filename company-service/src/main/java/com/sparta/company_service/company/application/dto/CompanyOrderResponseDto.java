package com.sparta.company_service.company.application.dto;

import com.sparta.company_service.company.domain.entity.Company;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyOrderResponseDto {

  private UUID companyId;
  private UUID hubId;
  private String companyAddress;

  public static CompanyOrderResponseDto toDto(Company company) {
    return CompanyOrderResponseDto.builder()
        .companyId(company.getId())
        .hubId(company.getHubId())
        .companyAddress(company.getAddress())
        .build();
  }
}
