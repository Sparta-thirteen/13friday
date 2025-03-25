package com.sparta.company_service.product.application.service;

import com.sparta.company_service.common.global.GlobalException;
import com.sparta.company_service.common.infrastructure.client.HubClient;
import com.sparta.company_service.common.infrastructure.dto.HubDto;
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
  private final HubClient hubClient;
  private final CompanyService companyService;

  @Transactional
  public void createProduct(String userId, String role, ProductRequestDto requestDto) {
    Company company = companyService.findCompany(requestDto.getCompanyId());
    createUpdateRoleCheck(Long.parseLong(userId), role, company);

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
    return productRepository.findByDeletedAtIsNull(pageable)
        .map(ProductResponseDto::toDto);
  }

  @Transactional(readOnly = true)
  public Page<ProductResponseDto> searchProducts(String keyword, Pageable pageable) {
    return productRepository.findAllByName(keyword, pageable)
        .map(ProductResponseDto::toDto);
  }

  @Transactional
  public void updateProduct(String userId, String role, UUID productId,
      ProductRequestDto requestDto) {
    Product product = findProduct(productId);
    Company company = companyService.findCompany(requestDto.getCompanyId());
    createUpdateRoleCheck(Long.parseLong(userId), role, company);
    UUID hubId = company.getHubId();
    product.update(requestDto, hubId);
  }

  @Transactional
  public void deleteProduct(String userId, String role, UUID productId) {
    Product product = findProduct(productId);
    deletedRoleCheck(Long.parseLong(userId), role, product);
    product.softDelete(Long.parseLong(userId));
  }

  private Product findProduct(UUID productId) {
    Product product = productRepository.findById(productId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));

    if (product.getDeletedAt() != null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "삭제된 상품입니다.");
    }

    return product;
  }

  private void createUpdateRoleCheck(Long userId, String role, Company company) {
    if (role.equals("COMPANYMANAGER") && !userId.equals(company.getUserId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "업체 담당자가 아닙니다.");
    }
    roleCheck(userId, role, company);
  }

  private void deletedRoleCheck(Long userId, String role, Product product) {
    if (role.equals("COMPANYMANAGER")) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
    Company company = companyService.findCompany(product.getCompanyId());
    roleCheck(userId, role, company);
  }

  private void roleCheck(Long userId, String role, Company company) {
    switch (role) {
      case "MASTER":
        return;
      case "SHIPPINGMANAGER":
        throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
      case "HUBMANAGER":
        UUID hubId = company.getHubId();
        HubDto.ResponseDto hubResponse = hubClient.getHub(hubId).getBody();

        if (!userId.equals(hubResponse.getUserId())) {
          throw new GlobalException(HttpStatus.FORBIDDEN, "허브 담당자가 아닙니다.");
        }
        break;
    }
  }
}
