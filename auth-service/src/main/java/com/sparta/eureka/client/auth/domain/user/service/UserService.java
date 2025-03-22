package com.sparta.eureka.client.auth.domain.user.service;

import com.sparta.eureka.client.auth.common.exception.ApiBusinessException;
import com.sparta.eureka.client.auth.domain.user.dto.request.UserRoleUpdateRequestDto;
import com.sparta.eureka.client.auth.domain.user.dto.response.PageInfoDto;
import com.sparta.eureka.client.auth.domain.user.dto.response.ReadResponseDto;
import com.sparta.eureka.client.auth.domain.user.dto.response.SearchUserResponseDto;
import com.sparta.eureka.client.auth.domain.user.dto.response.UserResponseDto;
import com.sparta.eureka.client.auth.domain.user.entity.Role;
import com.sparta.eureka.client.auth.domain.user.entity.User;
import com.sparta.eureka.client.auth.domain.user.exception.UserExceptionCode;
import com.sparta.eureka.client.auth.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  // 회원 목록 조회 & 검색기능 포함
  public SearchUserResponseDto searchUser(
      String role,
      String findUser,
      String slackId,
      String findRole,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable) {
    Role userRole = Role.valueOf(role);
    if(!userRole.isMaster()){
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    Page<User> findUsers = userRepository.findUsers(findUser, slackId, findRole, startDate, endDate, pageable);
    return SearchUserResponseDto.builder()
        .content(
            findUsers.getContent().stream()
                .map(ReadResponseDto::fromEntity)
                .collect(Collectors.toList()))
        .pageInfo(
            PageInfoDto.builder()
                .page(findUsers.getNumber())
                .size(findUsers.getSize())
                .totalElements(findUsers.getTotalElements())
                .totalPages(findUsers.getTotalPages())
                .build())
        .build();
  }

  //특정회원 조회
  public UserResponseDto getUser(Long id, String userId, String role) {
    // Master 권한이면 모든 회원 조회 가능
    Role userRole = Role.valueOf(role);
    if(userRole.isMaster()) {
      log.info("마스터인지 체크");
      return userRepository.findById(id)
          .map(UserResponseDto::fromEntity)
          .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));
    }

    // 그 외의 권한이면 본인 정보만 조회 가능
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    Long userIdLong = Long.valueOf(userId);
    if (!user.getUserId().equals(userIdLong)) {
      log.info("본인인지 체크");
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    return UserResponseDto.fromEntity(user);
  }

  //마스터가 다른 사람들의 권한 수정
  @Transactional
  public UserResponseDto updateUserRole(Long id, String role, UserRoleUpdateRequestDto userRoleUpdateRequestDto){
    Role userRole = Role.valueOf(role);
    if(!userRole.isMaster()){
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }
    User user = userRepository.findById(id)
        .orElseThrow(()->new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));
    user.updateRole(userRoleUpdateRequestDto.getRole());
    return UserResponseDto.fromEntity(user);
  }

  //마스터가 회원 삭제
  @Transactional
  public void deleteUser(String userId, String role, Long id){
    Role userRole = Role.valueOf(role);
    if(!userRole.isMaster()){
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    User user = userRepository.findById(id)
        .orElseThrow(()->new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    Long userIdLong = Long.valueOf(userId);
    user.delete(userIdLong);
  }
}