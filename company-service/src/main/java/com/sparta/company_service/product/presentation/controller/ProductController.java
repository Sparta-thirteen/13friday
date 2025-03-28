package com.sparta.company_service.product.presentation.controller;

import com.sparta.company_service.product.application.dto.ProductRequestDto;
import com.sparta.company_service.product.application.dto.ProductResponseDto;
import com.sparta.company_service.product.application.service.ProductService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<?> createProduct(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @RequestBody ProductRequestDto requestDto) {
    productService.createProduct(userId, role, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("상품 생성 성공");
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductResponseDto> getProduct(@PathVariable UUID productId) {
    ProductResponseDto responseDto = productService.getProduct(productId);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping
  public ResponseEntity<Page<ProductResponseDto>> getProducts(Pageable pageable) {
    Page<ProductResponseDto> responseDto = productService.getProducts(pageable);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<ProductResponseDto>> searchProducts(String keyword,
      Pageable pageable) {
    Page<ProductResponseDto> responseDto = productService.searchProducts(keyword, pageable);
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping("/{productId}")
  public ResponseEntity<?> updateProduct(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID productId,
      @RequestBody ProductRequestDto requestDto) {
    productService.updateProduct(userId, role, productId, requestDto);
    return ResponseEntity.status(HttpStatus.OK).body("상품 수정 성공");
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<?> deleteProduct(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID productId) {
    productService.deleteProduct(userId, role, productId);
    return ResponseEntity.status(HttpStatus.OK).body("상품 삭제 성공");
  }
}
