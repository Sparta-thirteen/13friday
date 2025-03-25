package com.sparta.company_service.product.application.service;

import com.sparta.company_service.common.global.GlobalException;
import com.sparta.company_service.common.infrastructure.dto.OrderItemsRequest;
import com.sparta.company_service.product.application.dto.ProductOrderRequestDto;
import com.sparta.company_service.product.application.dto.ProductOrderResponseDto;
import com.sparta.company_service.product.domain.entity.Product;
import com.sparta.company_service.product.domain.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

  public void updateProductStock(List<OrderItemsRequest> requestDto) {
    for (OrderItemsRequest orderItem : requestDto) {
      Product product = findProduct(orderItem.getProductId());
      checkStock(product, orderItem);
      product.updateStock(orderItem.getStock());
    }
  }

  private Product findProduct(UUID productId) {
    Product product = productRepository.findById(productId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다. ID: " + productId));

    if (product.getDeletedAt() != null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "삭제된 상품입니다.");
    }

    return product;
  }

  private void checkStock(Product product, OrderItemsRequest orderItem) {
    if (product.getStock() < orderItem.getStock()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "재고가 부족합니다. 상품 ID: " + product.getId());
    }
  }
}
