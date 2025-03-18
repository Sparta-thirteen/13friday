package com.sparta.company_service.company.application.dto;

import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.company.domain.entity.CompanyType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDto {

  private UUID companyId;
  private UUID hubId;
  private CompanyType type;
  private String name;
  private String address;

  public static CompanyResponseDto toDto(Company company) {
    return CompanyResponseDto.builder()
        .companyId(company.getId())
        .hubId(company.getHubId())
        .type(company.getType())
        .name(company.getName())
        .address(company.getAddress())
        .build();
  }
}
