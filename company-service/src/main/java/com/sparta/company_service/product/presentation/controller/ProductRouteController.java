package com.sparta.company_service.product.presentation.controller;

import com.sparta.company_service.product.application.dto.ProductOrderRequestDto;
import com.sparta.company_service.product.application.dto.ProductOrderResponseDto;
import com.sparta.company_service.product.application.service.ProductRouteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products-route")
@RequiredArgsConstructor
public class ProductRouteController {

  private final ProductRouteService productRouteService;

  @GetMapping("/orders")
  public List<ProductOrderResponseDto> getProductList(
      @RequestBody ProductOrderRequestDto requestDto) {
    return productRouteService.getProduct(requestDto);
  }
}
