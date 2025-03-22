package com.sparta.company_service.company.presentation.controller;

import com.sparta.company_service.company.application.service.CompanyRouteService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<?> updateCompanyUser(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID companyId) {
    companyRouteService.updateCompanyUser(userId, role, companyId);
    return ResponseEntity.ok().body("업체 담당자 지정 성공");
  }
}
