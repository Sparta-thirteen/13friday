package com.sparta.company_service.product.application.service;

import com.sparta.company_service.product.application.dto.ProductOrderRequestDto;
import com.sparta.company_service.product.application.dto.ProductOrderResponseDto;
import com.sparta.company_service.product.domain.entity.Product;
import com.sparta.company_service.product.domain.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductPublicService {

  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  public List<ProductOrderResponseDto> getProduct(ProductOrderRequestDto requestDto) {
    List<Product> productList = productRepository.findByIdIn(requestDto.getProductIdList());
    return productList.stream().map(ProductOrderResponseDto::toDto).toList();
  }
}
