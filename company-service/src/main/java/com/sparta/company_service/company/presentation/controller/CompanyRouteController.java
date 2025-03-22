package com.sparta.company_service.company.presentation.controller;

import com.sparta.company_service.company.application.dto.CompanyOrderRequestDto;
import com.sparta.company_service.company.application.dto.CompanyOrderResponseDto;
import com.sparta.company_service.company.application.service.CompanyRouteService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies-route")
@RequiredArgsConstructor
public class CompanyRouteController {

  private final CompanyRouteService companyRouteService;

  @PatchMapping("/{companyId}/users")
  public void updateCompanyUser(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID companyId) {
    companyRouteService.updateCompanyUser(userId, role, companyId);
  }

  @GetMapping("/orders")
  public CompanyOrderResponseDto getCompanyByName(CompanyOrderRequestDto requestDto) {
    return companyRouteService.getCompanyByName(requestDto);
  }
}
