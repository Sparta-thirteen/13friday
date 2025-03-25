package com.sparta.slack_service.common.infrastructure.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderInternalResponse {

  private String userName;
  private String email;
  private List<OrderItemsDto> orderItemsRequests;
  private String requestMessage;
  private UUID departHubId;
  private UUID arriveHubId;
  private Long hubUserID;
}
