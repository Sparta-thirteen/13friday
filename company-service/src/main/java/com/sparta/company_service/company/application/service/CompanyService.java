package com.sparta.company_service.company.application.service;

import com.sparta.company_service.company.application.dto.CompanyRequestDto;
import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;

  @Transactional
  public void createCompany(CompanyRequestDto requestDto) {
    // todo: 허브 id 검증 로직
    Company company = requestDto.toEntity();
    companyRepository.save(company);
  }
}
