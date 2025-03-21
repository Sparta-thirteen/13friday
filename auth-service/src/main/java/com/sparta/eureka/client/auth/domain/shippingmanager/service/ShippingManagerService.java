package com.sparta.eureka.client.auth.domain.shippingmanager.service;

import com.sparta.eureka.client.auth.common.exception.ApiBusinessException;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.request.CreateShippingManagerRequestDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.PageInfoDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.ReadResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.SearchShippingManagerResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.dto.response.ShippingManagerResponseDto;
import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManager;
import com.sparta.eureka.client.auth.domain.shippingmanager.repository.ShippingManagerRepository;
import com.sparta.eureka.client.auth.domain.user.entity.Role;
import com.sparta.eureka.client.auth.domain.user.entity.User;
import com.sparta.eureka.client.auth.domain.user.exception.UserExceptionCode;
import com.sparta.eureka.client.auth.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
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

  public void createShippingManager(String role,CreateShippingManagerRequestDto requestDto) {
    Role userRole = Role.valueOf(role);
    if(!userRole.isMaster()){
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    User user = userRepository.findById(requestDto.getUserId())
        .orElseThrow(()-> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // 현재 가장 마지막 배송순번 가져오기
    Optional<Integer> maxOrder = shippingManagerRepository.findMaxDeliveryOrderByType(requestDto.getShippingManagerType());
    int newOrder = (maxOrder.isEmpty()) ? 0 : maxOrder.orElse(null) + 1; // 첫 번째 등록이면 0, 아니면 maxOrder + 1

    //todo:hubId값 집어 넣을 수 있어야 함
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
      //todo: userId로 허브 검색해서 hubId받아 온다음에 집어 넣어야 함
      // HubManager는 본인의 hubId에 속한 ShippingManager만 검색 가능
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
    // todo: HUBID 허브도메인에서 찾아와야함 사용자의 userId활용해서
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
  public ShippingManagerResponseDto updateShippingManager(UUID shippingManagerId, String userId, String role){
    //todo: 배송순번 지정 타이밍이 언제인지 알아야하고 배송이 완료되면 순번을 재배치해야하나?
    return null;
  }

  @Transactional
  public void deleteShippingManager(UUID shippingManagerId, String userId, String role){
    Role userRole = Role.valueOf(role);

    // 삭제할 배송 담당자 조회
    ShippingManager shippingManager = shippingManagerRepository.findById(shippingManagerId)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    Long userIdLong = Long.valueOf(userId);

    // 마스터는 모든 배송담당자 삭제 가능
    if (userRole.isMaster()) {
      shippingManager.delete(userIdLong);
      return;
    }

    // 허브매니저는 같은 허브 소속의 배송담당자만 삭제 가능
    //todo: 얘도 허브아이디 찾아와서 넣어줘야함 지금은 임시로 값 넣어준거임
    UUID userHubId = shippingManager.getHubId();
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
}
