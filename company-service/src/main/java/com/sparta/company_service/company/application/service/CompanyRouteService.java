package com.sparta.company_service.company.application.service;

import com.sparta.company_service.common.global.GlobalException;
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
    company.updateUser(userId);
  }

  private void roleCheck(String role) {
    if (!role.equals("MASTER")) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
  }

  public Company findCompany(UUID companyId) {
    Company company = companyRepository.findById(companyId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."));

    if (company.getDeletedAt() != null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "삭제된 업체입니다.");
    }

    return company;
  }
}
