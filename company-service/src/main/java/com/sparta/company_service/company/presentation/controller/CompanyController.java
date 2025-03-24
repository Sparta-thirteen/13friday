package com.sparta.company_service.company.presentation.controller;

import com.sparta.company_service.company.application.dto.CompanyRequestDto;
import com.sparta.company_service.company.application.dto.CompanyResponseDto;
import com.sparta.company_service.company.application.service.CompanyService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @PostMapping
  public ResponseEntity<?> createCompany(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @RequestBody CompanyRequestDto requestDto) {
    companyService.createCompany(userId, role, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("업체 생성 성공");
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable UUID companyId) {
    CompanyResponseDto responseDto = companyService.getCompany(companyId);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping
  public ResponseEntity<Page<CompanyResponseDto>> getCompanies(Pageable pageable) {
    Page<CompanyResponseDto> responseDto = companyService.getCompanies(pageable);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<CompanyResponseDto>> searchCompanies(
      @RequestParam String keyword, Pageable pageable) {
    Page<CompanyResponseDto> responseDto = companyService.searchCompanies(keyword, pageable);
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID companyId,
      @RequestBody CompanyRequestDto requestDto) {
    companyService.updateCompany(userId, role, companyId, requestDto);
    return ResponseEntity.ok("업체 수정 성공");
  }

  @DeleteMapping("/{companyId}")
  public ResponseEntity<?> deleteCompany(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID companyId) {
    companyService.deleteCompany(userId, role, companyId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("업체 삭제 성공");
  }
}
