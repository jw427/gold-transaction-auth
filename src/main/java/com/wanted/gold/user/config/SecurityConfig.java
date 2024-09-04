package com.wanted.gold.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // 세션 미사용
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/users/**").permitAll() // 어떤 사용자든 접근 가능
                        .anyRequest().authenticated()) // 인증 필요
                .exceptionHandling(e -> e
                        // 인증 되지 않은 채로 인증 필요한 페이지 접근했을 경우
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.sendError(HttpStatus.UNAUTHORIZED.value(), "인증이 필요합니다.");
                        })
                        // 권한 없는데 권한이 필요한 페이지에 접근했을 경우
                        .accessDeniedHandler((request, response, exception) -> {
                            response.sendError(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다.");
                        }))
                // 현재 필터를 UsernamePasswordAuthenticationFilter 앞에 위치시켜 토큰 인증 먼저 수행
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
