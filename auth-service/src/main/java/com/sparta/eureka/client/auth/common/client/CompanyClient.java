package com.sparta.eureka.client.auth.common.client;

import com.sparta.eureka.client.auth.common.client.dto.CompanyRequestDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "company-service")
public interface CompanyClient {
  @PatchMapping("/api/companies-route/{companyId}/users")
  void updateCompany(@RequestHeader("X-Role") String role,
      @PathVariable("companyId") UUID companyId,
      @RequestBody CompanyRequestDto companyRequestDto);
}
