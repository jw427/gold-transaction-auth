package com.wanted.gold.user.config;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${JWT_SECRET_KEY}")
    private String key;
    @Value("${ACCESS_TOKEN_EXPIRATIO}")
    private long accessTokenValidTime;
    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long RefreshTokenValidTime;

    // 토큰 생성
    public String createToken(String username, String type) {
        // 현재 시간
        Date now = new Date();
        // token 타입이 access일 경우 accessToken 만료기간, refresh일 경우 refreshToken 만료기간
        long time = type.equals("access") ? accessTokenValidTime : RefreshTokenValidTime;
        // token 생성
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 타입
                .setClaims(Jwts.claims()
                        .setSubject(username) // 내용 sub : username
                        .setIssuedAt(now) // 내용 iat : 현재 시간
                        .setExpiration(new Date(now.getTime() + time)) // 내용 exp : 만료 시간
                )
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validToken(String token) {
        try {
            Jwts.parser().setSigningKey(key) // 토큰 검증하는데 사용되는 서명키 설정
                    .parseClaimsJws(token); // 토큰 파싱하고 서명 유효한지 확인
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
