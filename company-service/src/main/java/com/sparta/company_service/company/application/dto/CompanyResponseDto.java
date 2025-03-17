package com.sparta.company_service.company.application.dto;

import com.sparta.company_service.company.domain.entity.CompanyType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CompanyResponseDto {

  private UUID companyId;
  private UUID hubId;
  private CompanyType type;
  private String name;
  private String address;
}
