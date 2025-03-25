package com.sparta.eureka.client.auth.domain.auth.service;

import com.sparta.eureka.client.auth.domain.user.entity.Role;
import com.sparta.eureka.client.auth.domain.auth.dto.request.LoginRequestDto;
import com.sparta.eureka.client.auth.domain.auth.dto.request.SignUpRequestDto;
import com.sparta.eureka.client.auth.domain.auth.dto.response.LoginResponseDto;
import com.sparta.eureka.client.auth.common.jwt.JwtUtil;
import com.sparta.eureka.client.auth.domain.user.entity.User;
import com.sparta.eureka.client.auth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  //todo:환경변수로 관리하기
  private final String MASTER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

  public User signUp(SignUpRequestDto requestDto) {
    log.info("회원가입 시작!");
    Role role = Role.COMPANYMANAGER;// 회원가입을 처음 할때는 업체매니저로(가장 낮은 권한)
    //마스터토큰을 입력하면 마스터로 회원가입
    if(requestDto.getMasterToken().equals(MASTER_TOKEN)){
      role = Role.MASTER;
    }

    User.UserBuilder userBuilder = User.builder(
        requestDto.getUsername(),
        passwordEncoder.encode(requestDto.getPassword()),
        requestDto.getSlackId(),
        role
    );
    log.info("회원가입 객체: {}", userBuilder);
    User user = userRepository.save(userBuilder.build());
    log.info("저장된 회원 정보: {}", user);
    return user;
  }

  public LoginResponseDto login(LoginRequestDto requestDto) {
    User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
        ()->new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
    );

    if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("회원정보가 일치하지 않습니다.");
    }

    //토큰 생성
    String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getRole());

    return new LoginResponseDto(user.getUsername(), accessToken, user.getRole());
  }
}
