package com.sparta.company_service.company.presentation.controller;

import com.sparta.company_service.company.application.dto.CompanyResponseDto;
import com.sparta.company_service.company.application.service.CompanyPublicService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyPublicController {

  private final CompanyPublicService companyPublicService;

  @PatchMapping("/{companyId}/users")
  public void updateCompanyUser(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID companyId) {
    companyPublicService.updateCompanyUser(userId, role, companyId);
  }

  @GetMapping("/{companyId}")
  public CompanyResponseDto getCompany(@PathVariable UUID companyId) {
    return companyPublicService.getCompany(companyId);
  }

  @GetMapping("/name/{companyName}")
  public CompanyResponseDto getCompanyByName(@PathVariable String companyName) {
    return companyPublicService.getCompanyByName(companyName);
  }
}
