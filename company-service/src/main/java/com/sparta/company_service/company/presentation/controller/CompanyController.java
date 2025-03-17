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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @PostMapping
  public ResponseEntity<?> createCompany(CompanyRequestDto requestDto) {
    companyService.createCompany(requestDto);
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
}
