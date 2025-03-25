package com.sparta.slack_service.common.infrastructure.client;

import com.sparta.slack_service.common.infrastructure.dto.OrderInternalResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("order-service")
public interface OrderClient {

  @GetMapping("/api/orders/internal/{orderId}")
  OrderInternalResponse getOrdersInternal(@PathVariable UUID orderId);
}
