package com.sparta.eureka.client.auth.common.client;

import com.sparta.eureka.client.auth.domain.user.dto.request.CompanyRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "company-service")
public interface CompanyClient {
  @PatchMapping("/api/company/{userId}")
  void updateCompany(@RequestHeader("X-Role") String role,
      @PathVariable("userId") Long userId,
      @RequestBody CompanyRequestDto companyRequestDto);
}
