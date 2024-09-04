package com.wanted.gold.user.config;

import com.wanted.gold.user.domain.UserDetail;
import com.wanted.gold.user.service.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private final String BEARER_PREFIX = "Bearer ";

    private final UserDetailService userDetailService;

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

    // 토큰으로 인증 정보 담은 Authentication 반환
    public Authentication getAuthentication(String token) {
        UserDetail userDetail = (UserDetail) userDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities());
        /* principal : 인증된 사용자 정보
           credentials : 사용자의 인증 자격 증명 (인증 완료된 상태이므로 빈 문자열 사용)
           authorities : 사용자의 권한목록 */
    }

    // 토큰에서 username 추출
    public String getUsername(String token) {
        try {
            // JWT를 파싱해서 JWT 서명 검증 후 클레임을 반환하여 payload에서 subject 클레임 추출
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    // Header에서 토큰 가져오는 메서드
    public String resolveToken(HttpServletRequest request) {
        // Header의 Authorization 값 가져오기
        String header = request.getHeader("Authorization");
        // header 값이 null이 아니고 BEARER_PREFIX 값으로 시작하면 BEARER_PREFIX 이후의 값으로 반환
        if (header != null && header.startsWith(BEARER_PREFIX))
            return header.substring(BEARER_PREFIX.length());
        return null;
    }
}
