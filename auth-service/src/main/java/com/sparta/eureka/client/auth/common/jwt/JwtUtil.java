package com.sparta.eureka.client.auth.common.jwt;

import com.sparta.eureka.client.auth.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final String  AUTHORIZATION_KEY = "auth";
  private final long TOKEN_TIME = 60 * 60 * 1000L;

  @Value("${service.jwt.secret-key}")
  private String secretKey;
  private SecretKey key;

  @PostConstruct // 객체가 생성되고 의존성 주입이 완료된 후 자동으로 실행되는 메서드
  public void init() {
    // Base64로 인코딩된 secretKey를 디코딩하여 바이트 배열로 변환
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    // 변환된 바이트 배열을 사용하여 HMAC-SHA 키 객체 생성
    key = Keys.hmacShaKeyFor(bytes);
  }

  //토큰 생성
  public String createAccessToken(Long userId, Role role){
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .claim("user_id", userId)
            .claim(AUTHORIZATION_KEY, role)
            .expiration(new Date(date.getTime() + TOKEN_TIME))
            .issuedAt(date)
            .signWith(key)
            .compact();
  }

  // head4er에서 JWT 가져오기
  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
      return bearerToken.substring(7);
    }
    return null;
  }

  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token, 만료된 JWT token 입니다.");
      throw e;  // 토큰 만료 예외를 그대로 던짐
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

  //토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token){
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
