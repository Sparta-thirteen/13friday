package com.sparta.eureka.client.auth.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
  private static final String success = "SUCCESS";
  private static final String fail = "FAIL";

  private String status;
  private String message;
  private T data;

  //성공
  public static <T> BaseResponse<T> success(String message, T data) {
    return new BaseResponse<>(success, message, data);
  }

  public static <T> BaseResponse<T> success(String message) {
    return new BaseResponse<>(success, message, null);
  }

  //실패
  public static <T> BaseResponse<T> exception(String message, T errorCode) {
    return new BaseResponse<>(fail, message, errorCode);
  }
}