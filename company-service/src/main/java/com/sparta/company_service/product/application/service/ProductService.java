package com.sparta.company_service.product.application.service;

import com.sparta.company_service.company.application.service.CompanyService;
import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.product.application.dto.ProductRequestDto;
import com.sparta.company_service.product.domain.entity.Product;
import com.sparta.company_service.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CompanyService companyService;

  @Transactional
  public void createProduct(ProductRequestDto requestDto) {
    // todo: user 권한 검증 로직
    Company company = companyService.findCompany(requestDto.getCompanyId());
    Product product = requestDto.toEntity(company.getHubId());
    productRepository.save(product);
  }

}
