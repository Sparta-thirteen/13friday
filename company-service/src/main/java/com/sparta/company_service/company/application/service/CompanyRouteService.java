package com.sparta.company_service.company.application.service;

import com.sparta.company_service.common.global.GlobalException;
import com.sparta.company_service.company.application.dto.CompanyOrderRequestDto;
import com.sparta.company_service.company.application.dto.CompanyOrderResponseDto;
import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.company.domain.repository.CompanyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyRouteService {

  private final CompanyRepository companyRepository;

  @Transactional
  public void updateCompanyUser(String userId, String role, UUID companyId) {
    roleCheck(role);
    Company company = findCompany(companyId);
    deletedCheck(company);
    company.updateUser(userId);
  }

  @Transactional(readOnly = true)
  public CompanyOrderResponseDto getCompanyByName(CompanyOrderRequestDto requestDto) {
    Company company = findCompanyByName(requestDto.getCompanyName());
    return CompanyOrderResponseDto.toDto(company);
  }

  private void roleCheck(String role) {
    if (!role.equals("MASTER")) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
  }

  private Company findCompany(UUID companyId) {
    return companyRepository.findById(companyId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."));
  }

  private Company findCompanyByName(String name) {
    return companyRepository.findByName(name).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."));
  }

  private void deletedCheck(Company company) {
    if (company.getDeletedAt() != null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "삭제된 업체입니다.");
    }
  }
}
