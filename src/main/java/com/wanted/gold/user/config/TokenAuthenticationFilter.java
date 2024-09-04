package com.wanted.gold.user.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Header에서 가져온 accessToken
        String token = tokenProvider.resolveToken(request);
        // 토큰 유효성 검증
        if(token != null && tokenProvider.validToken(token)) {
            // 인증 정보 담은 Authentication 객체
            Authentication authentication = tokenProvider.getAuthentication(token);
            // 인증 정보를 보안 컨텍스트에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 응답과 요청을 다음 필터로 전달
        filterChain.doFilter(request, response);
    }
}
