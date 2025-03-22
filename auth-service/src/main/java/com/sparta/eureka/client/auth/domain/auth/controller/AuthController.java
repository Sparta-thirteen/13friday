package com.sparta.eureka.client.auth.domain.auth.controller;

import com.sparta.eureka.client.auth.domain.auth.dto.request.LoginRequestDto;
import com.sparta.eureka.client.auth.domain.auth.dto.request.SignUpRequestDto;
import com.sparta.eureka.client.auth.domain.auth.dto.response.LoginResponseDto;
import com.sparta.eureka.client.auth.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody SignUpRequestDto requestDto){
    log.info("authService 하러 가자!");
    authService.signUp(requestDto);
    log.info("authService의 signUp 완료함");
    Map<String, String> response = new HashMap<>();
    response.put("message", "회원가입이 완료되었습니다.");
    log.info("response : " + response);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto){
    LoginResponseDto loginResponseDto = authService.login(requestDto);
    return ResponseEntity.ok(loginResponseDto);
  }
}
