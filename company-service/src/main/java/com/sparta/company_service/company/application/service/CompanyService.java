package com.sparta.company_service.company.application.service;

import com.sparta.company_service.common.global.GlobalException;
import com.sparta.company_service.company.application.dto.CompanyRequestDto;
import com.sparta.company_service.company.application.dto.CompanyResponseDto;
import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.company.domain.repository.CompanyRepository;
import com.sparta.company_service.product.application.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final ProductService productService;

  @Transactional
  public void createCompany(CompanyRequestDto requestDto) {
    // todo: user 권한 검증 로직
    // todo: 허브 id 검증 로직
    Company company = requestDto.toEntity();
    companyRepository.save(company);
  }

  @Transactional(readOnly = true)
  public CompanyResponseDto getCompany(UUID companyId) {
    Company company = findCompany(companyId);
    return CompanyResponseDto.toDto(company);
  }

  @Transactional(readOnly = true)
  public Page<CompanyResponseDto> getCompanies(Pageable pageable) {
    return companyRepository.findByDeletedAtIsNull(pageable)
        .map(CompanyResponseDto::toDto);
  }

  @Transactional(readOnly = true)
  public Page<CompanyResponseDto> searchCompanies(String keyword, Pageable pageable) {
    return companyRepository.findAllByName(keyword, pageable)
        .map(CompanyResponseDto::toDto);
  }

  @Transactional
  public void updateCompany(UUID companyId, CompanyRequestDto requestDto) {
    // todo: user 권한 검증 로직, hubId 검증 로직
    Company company = findCompany(companyId);
    company.update(requestDto);
  }

  @Transactional
  public void deleteCompany(UUID companyId) {
    // todo: user 권한 검증 로직
    Company company = findCompany(companyId);
    productService.softDeleteByCompanyId(companyId);
    company.softDelete();
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
