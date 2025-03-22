package com.sparta.eureka.client.auth.domain.user.entity;

public enum Role {
  MASTER("MASTER"),
  HUBMANAGER("HUBMANAGER"),
  SHIPPINGMANGER("SHIPPINGMANAGER"),
  COMPANYMANAGER("COMPANYMANAGER");

  private final String authority;

  Role(String authority){
    this.authority = authority;
  }

  public String getAuthority(){
    return this.authority;
  }

  public boolean isMaster(){
    return this == Role.MASTER;
  }

  public boolean isHubManager(){
    return this == Role.HUBMANAGER;
  }

  public boolean isShippingManager(){
    return this == Role.SHIPPINGMANGER;
  }

  public boolean isCompanyManager(){
    return this == Role.COMPANYMANAGER;
  }
}
