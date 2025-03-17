package com.sparta.company_service.company.presentation.controller;

import com.sparta.company_service.company.application.dto.CompanyRequestDto;
import com.sparta.company_service.company.application.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
