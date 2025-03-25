package com.sparta.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
  @Value("${service.jwt.secret-key}")
  private String secretKey;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    //회원가입과 로그인은 jwt없어도 가능해야하므로 해당 엔드포인트면 넘김
    if(path.equals("/api/auth/signup")||path.equals("/api/auth/login") || path.matches(".*/v3/api-docs.*") || path.matches(".*/swagger-ui/.*")) {
      return chain.filter(exchange);
    }
    //헤더에서 토큰 꺼내기
    String token = extractToken(exchange);
    //토큰에서 claims 꺼내기
    Claims claims = parseToken(token);
    //토큰, 클레임이 null이거나 만료기간이 현재시간보다 이전이면 401에러 발생
    if (token == null || claims == null || claims.getExpiration().before(new Date())) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    //제어문을 통과하면 클레임과 exchange로 새로운 exchange생성한후 다음 filter로 넘김
    ServerWebExchange newExchange = createNewExchange(claims, exchange);
    return chain.filter(newExchange);
  }

  //요청헤더에서 토큰 꺼내기
  private String extractToken(ServerWebExchange exchange) {
    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
    if(authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  //토큰에서 Claims 추출하기
  private Claims parseToken(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
      Jws<Claims> claimsJws = Jwts.parser()
          .verifyWith(key)
          .build().parseSignedClaims(token);

      Claims claims = claimsJws.getPayload();

      Date expiration = claims.getExpiration();
      if (expiration != null && expiration.before(new Date())) {
        log.warn("JWT Token 만료되었습니다.");
        return null;
      }

      log.info("#####payload :: " + claims.toString());
      return claims;
    } catch (Exception e) {
      log.warn("JWT 검증에 실패 했습니다!: {}", e.getMessage());
    }
    return null;
  }

  //request에 userId, role을 담아서 새로운 exchange생성
  private ServerWebExchange createNewExchange(Claims claims, ServerWebExchange exchange) {
    /*mutate()기존 request를 기반으로 새로운 인스턴스 생성할 준비함
      userId, role을 추가하고
      build()하여 새로운 request생성
    * */
    ServerHttpRequest newRequest = exchange.getRequest().mutate()
        .header("X-User-Id", claims.get("user_id").toString())
        .header("X-Role", claims.get("auth").toString())
        .build();
    /*
    mutate()기존 exchange를 기반으로 새로운 인스턴스 생성할 준비함
    request(newRequest) 새로운 요청을 설정한 후
    build()로 새로운 exchange를 생성함
     */
    return exchange.mutate().request(newRequest).build();
  }


}
