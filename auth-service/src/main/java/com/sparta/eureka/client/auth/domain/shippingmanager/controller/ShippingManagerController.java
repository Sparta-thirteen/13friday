package com.sparta.eureka.client.auth.domain.shippingmanager.controller;

import com.sparta.eureka.client.auth.common.dto.BaseResponse;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.request.CreateShippingManagerRequestDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.SearchShippingManagerResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.ShippingManagerResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.service.ShippingManagerService;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courier")
public class ShippingManagerController {
  private final ShippingManagerService shippingManagerService;

  @PostMapping
  public ResponseEntity<BaseResponse<?>> createShippingManager(
      @RequestBody CreateShippingManagerRequestDto createShippingManagerRequestDto,
      @RequestHeader("X-Role") String role) {
    shippingManagerService.createShippingManager(role, createShippingManagerRequestDto);
    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("배송 담당자 생성 성공"));
  }

  @GetMapping
  public ResponseEntity<BaseResponse<SearchShippingManagerResponseDto>> getShippingManagers(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @RequestParam(required = false) UUID shippingManagerId,
      @RequestParam(required = false) UUID hubId,
      @RequestParam(required = false)
      LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable
  ){
    SearchShippingManagerResponseDto searchShippingManagerResponseDto =
        shippingManagerService.searchShippingManager(userId,role, shippingManagerId, hubId, startDate, endDate, pageable);
    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.success("배송 담당자 목록 조회 성공", searchShippingManagerResponseDto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse<ShippingManagerResponseDto>> getShippingManager(
      @PathVariable("id") UUID shippingManagerId,
      @RequestHeader("X-Role") String role,
      @RequestHeader("X-User-Id") String userId
  ){
    ShippingManagerResponseDto shippingManagerResponseDto =
        shippingManagerService.getShippingManager(shippingManagerId, userId, role);
    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.success("특정 배송담당자 조회 성공", shippingManagerResponseDto));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<BaseResponse> updateShippingManager(
      @PathVariable("id") UUID shippingManagerId,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role
  ){
    shippingManagerService.updateShippingManager(shippingManagerId,userId, role);
    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("배송순번 변경 성공"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BaseResponse> deleteShippingManager(
      @PathVariable("id") UUID shippingManagerId,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role
  ){
    shippingManagerService.deleteShippingManager(shippingManagerId, userId, role);
    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("배송담당자 삭제 성공"));

  }
}
