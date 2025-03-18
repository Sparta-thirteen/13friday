package com.sparta.company_service.product.presentation.controller;

import com.sparta.company_service.product.application.dto.ProductRequestDto;
import com.sparta.company_service.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<?> createProduct(@RequestBody ProductRequestDto requestDto) {
    productService.createProduct(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("상품 생성 성공");
  }
}
