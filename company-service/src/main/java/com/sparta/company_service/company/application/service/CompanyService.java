package com.sparta.company_service.company.application.service;

import com.sparta.company_service.common.global.GlobalException;
import com.sparta.company_service.common.infrastructure.client.HubClient;
import com.sparta.company_service.common.infrastructure.dto.HubDto;
import com.sparta.company_service.company.application.dto.CompanyRequestDto;
import com.sparta.company_service.company.application.dto.CompanyResponseDto;
import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.company.domain.repository.CompanyRepository;
import com.sparta.company_service.product.domain.repository.ProductRepository;
import java.time.LocalDateTime;
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
  private final ProductRepository productRepository;
  private final HubClient hubClient;

  @Transactional
  public void createCompany(String userId, String role, CompanyRequestDto requestDto) {
    HubDto.ResponseDto hubResponse = hubClient.getHub(requestDto.getHubId()).getBody();
    createdDeletedRoleCheck(Long.parseLong(userId), role, hubResponse.getUserId());

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
  public void updateCompany(String userId, String role, UUID companyId,
      CompanyRequestDto requestDto) {
    Company company = findCompany(companyId);

    HubDto.ResponseDto hubResponse = hubClient.getHub(requestDto.getHubId()).getBody();
    updatedRoleCheck(Long.parseLong(userId), role, hubResponse.getUserId(), company.getUserId());

    company.update(requestDto);
  }

  @Transactional
  public void deleteCompany(String userId, String role, UUID companyId) {
    Company company = findCompany(companyId);

    UUID hubId = company.getHubId();
    HubDto.ResponseDto hubResponse = hubClient.getHub(hubId).getBody();
    createdDeletedRoleCheck(Long.parseLong(userId), role, hubResponse.getUserId());

    productRepository.softDeleteByCompanyId(companyId, LocalDateTime.now());
    company.softDelete(Long.parseLong(userId));
  }

  public Company findCompany(UUID companyId) {
    Company company = companyRepository.findById(companyId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."));

    if (company.getDeletedAt() != null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "삭제된 업체입니다.");
    }

    return company;
  }

  private void createdDeletedRoleCheck(Long userId, String role, Long hubUserId) {
    if (role.equals("COMPANYMANAGER")) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
    roleCheck(userId, role, hubUserId);
  }

  private void updatedRoleCheck(Long userId, String role, Long hubUserId, Long companyUserId) {
    if (role.equals("COMPANYMANAGER") && !userId.equals(companyUserId)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "업체 담당자가 아닙니다.");
    }
    roleCheck(userId, role, hubUserId);
  }

  private void roleCheck(Long userId, String role, Long hubUserId) {
    if (role.equals("SHIPPINGMANAGER")) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
    if (role.equals("HUBMANAGER") && !userId.equals(hubUserId)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "허브 담당자가 아닙니다.");
    }
  }
}
