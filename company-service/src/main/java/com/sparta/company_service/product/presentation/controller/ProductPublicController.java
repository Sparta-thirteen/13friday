package com.sparta.company_service.product.presentation.controller;

import com.sparta.company_service.common.infrastructure.dto.OrderItemsRequest;
import com.sparta.company_service.product.application.dto.ProductOrderRequestDto;
import com.sparta.company_service.product.application.dto.ProductOrderResponseDto;
import com.sparta.company_service.product.application.service.ProductPublicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
@Slf4j
public class ProductPublicController {

  private final ProductPublicService productPublicService;

  @GetMapping("/orders")
  public List<ProductOrderResponseDto> getProductList(
      @RequestBody ProductOrderRequestDto requestDto) {
    return productPublicService.getProduct(requestDto);
  }

  @PostMapping("/orders")
  public void updateProductStock(@RequestBody List<OrderItemsRequest> requestDto) {
    productPublicService.updateProductStock(requestDto);
  }
}
