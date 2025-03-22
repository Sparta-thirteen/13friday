package com.sparta.eureka.client.auth.domain.user.controller;

import com.sparta.eureka.client.auth.common.dto.BaseResponse;
import com.sparta.eureka.client.auth.domain.user.dto.request.UserRoleUpdateRequestDto;
import com.sparta.eureka.client.auth.domain.user.dto.response.SearchUserResponseDto;
import com.sparta.eureka.client.auth.domain.user.dto.response.UserResponseDto;
import com.sparta.eureka.client.auth.domain.user.service.UserService;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse<UserResponseDto>> getUser(@PathVariable Long id,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role) {
    UserResponseDto userResponseDto = userService.getUser(id, userId, role);
    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("특정 회원 조회 성공", userResponseDto));
  }

  @GetMapping
  public ResponseEntity<BaseResponse<SearchUserResponseDto>> getUsers(
      @RequestHeader("X-Role") String role,
      @RequestParam(required = false) String findUser,
      @RequestParam(required = false) String slackId,
      @RequestParam(required = false) String findRole,
      @RequestParam(required = false)
      LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable){
    SearchUserResponseDto searchUserResponseDto =
        userService.searchUser(role, findUser, slackId, findRole, startDate, endDate, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("회원 목록 조회 성공", searchUserResponseDto));
  }

  @PatchMapping("/{id}/")
  public ResponseEntity<BaseResponse<UserResponseDto>> updateUser(
      @PathVariable Long id,
      @RequestHeader("X-Role") String role,
      @RequestParam(required = false) UUID hubID,
      @RequestBody UserRoleUpdateRequestDto userRoleUpdateRequestDto){
    UserResponseDto userResponseDto = userService.updateUserRole(id, role, hubID, userRoleUpdateRequestDto);
    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("회원 권한 변경 성공",userResponseDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BaseResponse<?>> deleteUser(
      @PathVariable Long id,
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role){
    userService.deleteUser(userId, role, id);
    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("회원 삭제 성공"));
  }
}
