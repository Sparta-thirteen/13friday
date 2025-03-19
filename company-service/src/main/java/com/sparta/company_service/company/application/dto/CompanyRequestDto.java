package com.sparta.company_service.company.application.dto;

import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.company.domain.entity.CompanyType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CompanyRequestDto {

  private UUID hubId;
  private CompanyType type;
  private String name;
  private String address;

  public Company toEntity() {
    return Company.builder()
        .hubId(this.hubId)
        .type(this.type)
        .name(this.name)
        .address(this.address)
        .build();
  }
}
