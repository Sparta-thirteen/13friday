package com.sparta.deliveryservice.presentation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
//            .authorizeHttpRequests(auth -> auth
//
//                // 인증 없이 접근 가능
//                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**", "/")
//                .permitAll()
//
//                // [배송 API] 관리자(Master) 또는 매니저(Manager)만 추가 가능, 점주(Owner)는 업데이트 가능
//                .requestMatchers(HttpMethod.PATCH, "/api/deliveries").hasAnyAuthority(
//                    Authority.MASTER, Authority.HUB_MANAGER, Authority.DELIVERY_AGENT)
//                .requestMatchers(HttpMethod.DELETE, "/api/deliveries").hasAnyAuthority(
//                    Authority.MASTER, Authority.HUB_MANAGER)
//
//                .requestMatchers(HttpMethod.GET, "/api/deliveries/**").permitAll()
//
//                // 기본적으로 모든 요청은 인증이 필요함
//                .anyRequest().authenticated()
//
//            )

            .formLogin(form -> form.disable())  // 로그인 폼 비활성화
            .httpBasic(basic -> basic.disable());  // 기본 인증 비활성화

        return http.build();
    }
}

