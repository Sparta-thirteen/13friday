package com.sparta.company_service.company.presentation.controller;

import com.sparta.company_service.company.application.dto.CompanyResponseDto;
import com.sparta.company_service.company.application.service.CompanyPublicService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/companies")
@RequiredArgsConstructor
public class CompanyPublicController {

  private final CompanyPublicService companyPublicService;

  @PostMapping("/{companyId}/users/{userId}")
  public void updateCompanyUser(
      @PathVariable Long userId,
      @PathVariable UUID companyId) {
    companyPublicService.updateCompanyUser(userId, companyId);
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
