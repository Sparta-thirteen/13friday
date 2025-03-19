package com.sparta.company_service.product.application.service;

import com.sparta.company_service.common.global.GlobalException;
import com.sparta.company_service.company.application.service.CompanyService;
import com.sparta.company_service.company.domain.entity.Company;
import com.sparta.company_service.product.application.dto.ProductRequestDto;
import com.sparta.company_service.product.application.dto.ProductResponseDto;
import com.sparta.company_service.product.domain.entity.Product;
import com.sparta.company_service.product.domain.repository.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

  @Transactional(readOnly = true)
  public ProductResponseDto getProduct(UUID productId) {
    Product product = findProduct(productId);
    return ProductResponseDto.toDto(product);
  }

  @Transactional(readOnly = true)
  public Page<ProductResponseDto> getProducts(Pageable pageable) {
    return productRepository.findAll(pageable)
        .map(ProductResponseDto::toDto);
  }

  @Transactional(readOnly = true)
  public Page<ProductResponseDto> searchProducts(String keyword, Pageable pageable) {
    return productRepository.findAllByName(keyword, pageable)
        .map(ProductResponseDto::toDto);
  }

  @Transactional
  public void updateProduct(UUID productId, ProductRequestDto requestDto) {
    // todo: user 권한 검증 로직, deleteAt 검증 로직
    Product product = findProduct(productId);
    Company company = companyService.findCompany(requestDto.getCompanyId());
    UUID hubId = company.getHubId();
    product.update(requestDto, hubId);
  }

  @Transactional
  public void deleteProduct(UUID productId) {
    // todo: user 권한 검증 로직
    Product product = findProduct(productId);
  }

  private Product findProduct(UUID productId) {
    return productRepository.findById(productId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));
  }
}
