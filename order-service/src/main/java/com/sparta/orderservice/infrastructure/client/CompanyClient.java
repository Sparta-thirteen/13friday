package com.sparta.orderservice.infrastructure.client;

import com.sparta.orderservice.presentation.requset.ProductOrderRequestDto;
import com.sparta.orderservice.presentation.response.CompanyResponseDto;
import com.sparta.orderservice.presentation.response.ProductOrderResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "company-service")
public interface CompanyClient {


    @GetMapping("/api/companies-route/name/{companyName}")
    CompanyResponseDto getCompanyByName(@PathVariable String companyName);


    @GetMapping("/api/products-route/orders")
    List<ProductOrderResponseDto> getProductList(@RequestBody ProductOrderRequestDto requestDto);

}
