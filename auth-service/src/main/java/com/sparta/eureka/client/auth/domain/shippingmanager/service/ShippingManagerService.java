package com.sparta.eureka.client.auth.domain.shippingmanager.service;

import com.sparta.eureka.client.auth.common.exception.ApiBusinessException;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.request.CreateShippingManagerRequestDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.request.DeliveryRequestDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.PageInfoDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.ReadResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.SearchShippingManagerResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.ShippingManagerResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManager;
import com.sparta.eureka.client.auth.domain.shippingmanager.repository.ShippingManagerRepository;
import com.sparta.eureka.client.auth.common.client.HubClient;
import com.sparta.eureka.client.auth.common.client.dto.HubIdResponseDto;
import com.sparta.eureka.client.auth.domain.user.entity.Role;
import com.sparta.eureka.client.auth.domain.user.entity.User;
import com.sparta.eureka.client.auth.domain.user.exception.UserExceptionCode;
import com.sparta.eureka.client.auth.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShippingManagerService {
  private final ShippingManagerRepository shippingManagerRepository;
  private final UserRepository userRepository;
  private final HubClient hubClient;

  public void createShippingManager(String role,CreateShippingManagerRequestDto requestDto) {
    //현재 사용자가 마스터권한인지 체크
    Role userRole = Role.valueOf(role);
    if(!userRole.isMaster()){
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    //특정 유저를 찾고 그 사람의 권한 SHIPPINGMANAGER로 변경
    User user = userRepository.findById(requestDto.getUserId())
        .orElseThrow(()-> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));
    user.updateRole(Role.SHIPPINGMANGER);

    //배송순번 지정
    Integer newOrder;

    if (requestDto.getHubId() != null) {
      // 허브에 속한 경우 (COMPANYSHIPPING) → 같은 허브 내에서 deliveryOrder 찾기
      Optional<Integer> maxOrder = shippingManagerRepository.findMaxDeliveryOrderByHubId(requestDto.getHubId());
      newOrder = maxOrder.orElse(-1) + 1; // 최초 생성이면 0부터 시작
    } else {
      // 허브에 속하지 않은 경우 (HUBSHIPPING) → hubId가 NULL인 사람들 중 maxOrder 찾기
      Optional<Integer> maxOrder = shippingManagerRepository.findMaxDeliveryOrderForHubShipping();
      newOrder = maxOrder.orElse(-1) + 1; // 최초 생성이면 0부터 시작
    }

    ShippingManager shippingManager = ShippingManager.builder(
        requestDto.getUserId(),
        requestDto.getSlackId(),
        requestDto.getHubId(),
        requestDto.getShippingManagerType(),
        newOrder
    ).build();

    shippingManagerRepository.save(shippingManager);
  }

  public SearchShippingManagerResponseDto searchShippingManager(
      String userId,
      String role,
      UUID shippingManagerId,
      UUID hubId,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable){
    //마스터 혹은 허브매니저가 아니면 예외처리
    Role userRole = Role.valueOf(role);
    if(!(userRole.isMaster() || userRole.isHubManager())){
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY, userId, role);
    }

    // 검색 조건 설정
    if (userRole.isMaster()) {
      // Master는 모든 ShippingManager 검색 가능
      Page<ShippingManager> shippingManagers = shippingManagerRepository.findShippingManagers(
          shippingManagerId, hubId, startDate, endDate, pageable);
      return SearchShippingManagerResponseDto.builder()
          .content(
              shippingManagers.getContent().stream()
                  .map(ReadResponseDto::fromEntity)
                  .collect(Collectors.toList()))
          .pageInfo(
              PageInfoDto.builder()
                  .page(shippingManagers.getNumber())
                  .size(shippingManagers.getSize())
                  .totalElements(shippingManagers.getTotalElements())
                  .totalPages(shippingManagers.getTotalPages())
                  .build())
          .build();
    } else if (userRole.isHubManager()) {
      //허브클라이언트로 허브정보 가져오기
      HubIdResponseDto hubInfo = hubClient.getHubByManager(userId);
      UUID searchHubId =hubInfo.getHubId();
      Page<ShippingManager> shippingManagers = shippingManagerRepository.findShippingManagers(
          shippingManagerId, searchHubId, startDate, endDate, pageable);

      return SearchShippingManagerResponseDto.builder()
          .content(
              shippingManagers.getContent().stream()
                  .map(ReadResponseDto::fromEntity)
                  .collect(Collectors.toList()))
          .pageInfo(
              PageInfoDto.builder()
                  .page(shippingManagers.getNumber())
                  .size(shippingManagers.getSize())
                  .totalElements(shippingManagers.getTotalElements())
                  .totalPages(shippingManagers.getTotalPages())
                  .build())
          .build();
    }

    throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY, userId, role);
  }

  public ShippingManagerResponseDto getShippingManager(UUID shippingManagerId, String userId, String role){
    Role userRole = Role.valueOf(role);
    // 배송담당자 정보 조회
    ShippingManager shippingManager = shippingManagerRepository.findById(shippingManagerId)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // Master는 모든 배송담당자 조회 가능
    if (userRole.isMaster()) {
      return ShippingManagerResponseDto.fromEntity(shippingManager);
    }

    // HubManager는 자신의 hubId에 속한 배송담당자만 조회 가능
    UUID userHubId = shippingManager.getHubId();
    if (userRole.isHubManager()) {
      if (!shippingManager.getHubId().equals(userHubId)) {
        throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
      }
      return ShippingManagerResponseDto.fromEntity(shippingManager);
    }

    // 배송담당자는 본인 정보만 조회 가능
    if (userRole.isShippingManager()) {
      if (!shippingManager.getUserId().toString().equals(userId)) {
        throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
      }
      return ShippingManagerResponseDto.fromEntity(shippingManager);
    }

    // 그 외의 역할은 조회 권한 없음
    throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
  }

  @Transactional
  public void deleteShippingManager(UUID shippingManagerId, String userId, String role){
    //현재 사용자 권한
    Role userRole = Role.valueOf(role);

    // 삭제할 배송 담당자 조회
    ShippingManager shippingManager = shippingManagerRepository.findById(shippingManagerId)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    //deleteBy에 집어넣을 userId(즉 현재사용자)
    Long userIdLong = Long.valueOf(userId);

    // 마스터는 모든 배송담당자 삭제 가능
    if (userRole.isMaster()) {
      shippingManager.delete(userIdLong);
      return;
    }

    // 허브매니저는 같은 허브 소속의 배송담당자만 삭제 가능
    UUID userHubId = hubClient.getHubByManager(userId).getHubId();
    if (userRole.isHubManager()) {
      if (!shippingManager.getHubId().equals(userHubId)) {
        throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
      }
      shippingManager.delete(userIdLong);
      return;
    }

    // 그 외의 역할은 삭제 권한 없음
    throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
  }

  public ShippingManagerResponseDto getShippingManagerByDeliveryOrder(DeliveryRequestDto deliveryRequestDto) {
    //요청에 hubId가 없으면
    if(deliveryRequestDto.getHubId() == null){
      ShippingManager shippingManager = shippingManagerRepository.findByHubIdIsNullAndDeliveryOrder(
          deliveryRequestDto.getDeliveryOrder()).orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));
      return ShippingManagerResponseDto.fromEntity(shippingManager);
    } else{
      //요청에 hubId가 있으면
      ShippingManager shippingManager = shippingManagerRepository.findByHubIdAndDeliveryOrder(
          deliveryRequestDto.getHubId(), deliveryRequestDto.getDeliveryOrder()).orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));
      return ShippingManagerResponseDto.fromEntity(shippingManager);
    }
  }
}
