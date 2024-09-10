package com.wanted.gold.user.repository;

import com.wanted.gold.user.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {
    // 리프레시 토큰과 회원의 식별번호로 Token 객체 찾기
    Optional<Token> findByRefreshTokenAndUser_UserId(String refreshToken, UUID userId);
}
