package com.sparta.company_service.company.controller;

import com.sparta.company_service.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;
}
