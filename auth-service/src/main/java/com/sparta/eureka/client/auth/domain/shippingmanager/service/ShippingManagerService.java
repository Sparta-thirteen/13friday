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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    // 삭제된 배송담당자인지 확인
    if(shippingManager.getDeletedAt() != null){
      log.info("삭제된 배송 담당자");
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND);
    }
    // Master는 모든 배송담당자 조회 가능
    if (userRole.isMaster()) {
      return ShippingManagerResponseDto.fromEntity(shippingManager);
    }

    // HubManager는 자신의 hubId에 속한 배송담당자만 조회 가능
    UUID userHubId = shippingManager.getHubId();
    if (userRole.isHubManager()) {
      if (shippingManagerId != userHubId) {
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
    log.info("shippingManagerSerivce 들어옴");
    UUID hubId = deliveryRequestDto.getHubId();
    int deliveryOrder = deliveryRequestDto.getDeliveryOrder();
    log.info("deliveryOrder : " + deliveryOrder);
    ShippingManager shippingManager = findNextAvailableShippingManager(hubId, deliveryOrder, new HashSet<>());
    log.info(shippingManager.getId().toString());
    log.info(String.valueOf(shippingManager.getDeliveryOrder()));
    return ShippingManagerResponseDto.fromEntity(shippingManager);
  }


  private ShippingManager findNextAvailableShippingManager(UUID hubId, int deliveryOrder, Set<Integer> visitedOrders) {
    log.info("가능한 배송담당자 찾기");
    // 최대 순번 가져오기
    Integer maxDeliveryOrder = hubId == null
        ? shippingManagerRepository.findMaxDeliveryOrderByHubIdIsNull().orElse(-1)
        : shippingManagerRepository.findMaxDeliveryOrderByHubId(hubId).orElse(-1);

    // 유효한 ShippingManager가 아예 없으면 바로 예외 발생
    if (maxDeliveryOrder == -1) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND);
    }

    // 순환 방지: 이미 방문한 순번이면 예외 발생
    if (!visitedOrders.add(deliveryOrder)) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND); // 모든 순번을 탐색했음
    }

    // 순번이 최대값 초과하면 0부터 다시 탐색
    if (deliveryOrder > maxDeliveryOrder) {
      deliveryOrder = 0;
      // **초기화 전에 방문 여부 체크 (중복 검사 최적화)**
      if (!visitedOrders.add(deliveryOrder)) {
        throw new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND); // 모든 순번 탐색 완료
      }
    }

    // ShippingManager 조회
    Optional<ShippingManager> optionalShippingManager = hubId == null
        ? shippingManagerRepository.findByHubIdIsNullAndDeliveryOrder(deliveryOrder)
        : shippingManagerRepository.findByHubIdAndDeliveryOrder(hubId, deliveryOrder);

    ShippingManager shippingManager = optionalShippingManager.orElseThrow(() ->
        new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // 삭제된 사람이라면 다음 순번으로 이동
    if (shippingManager.getDeletedAt() != null) {
      int nextDeliveryOrder = (deliveryOrder + 1 > maxDeliveryOrder) ? 0 : deliveryOrder + 1;

      // **재귀 호출 전에 방문 여부 체크 (불필요한 호출 방지)**
      if (!visitedOrders.add(nextDeliveryOrder)) {
        throw new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND);
      }

      return findNextAvailableShippingManager(hubId, nextDeliveryOrder, visitedOrders);
    }

    return shippingManager;
  }

}
