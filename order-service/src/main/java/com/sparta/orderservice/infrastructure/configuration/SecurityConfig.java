package com.sparta.orderservice.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
//                // [주문 API]
//                .requestMatchers(HttpMethod.PATCH, "/api/orders")
//                .permitAll()
//
//                .requestMatchers(HttpMethod.PATCH, "/api/orders").hasAnyAuthority(
//                    Authority.MASTER, Authority.HUB_MANAGER)
//                .requestMatchers(HttpMethod.DELETE, "/api/orders").hasAnyAuthority(
//                    Authority.MASTER, Authority.HUB_MANAGER)
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
