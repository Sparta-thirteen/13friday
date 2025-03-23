package com.sparta.slack_service.common.infrastructure.dto;

public enum Role {
  MASTER("MASTER"),
  HUBMANAGER("HUBMANAGER"),
  SHIPPINGMANGER("SHIPPINGMANAGER"),
  COMPANYMANAGER("COMPANYMANAGER");

  private final String authority;

  Role(String authority) {
    this.authority = authority;
  }
}
