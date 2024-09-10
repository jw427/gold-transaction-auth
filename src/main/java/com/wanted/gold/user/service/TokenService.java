package com.wanted.gold.user.service;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.NotFoundException;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.user.config.TokenProvider;
import com.wanted.gold.user.domain.Token;
import com.wanted.gold.user.domain.User;
import com.wanted.gold.user.domain.UserDetail;
import com.wanted.gold.user.dto.TokenRequestDto;
import com.wanted.gold.user.dto.TokenResponseDto;
import com.wanted.gold.user.repository.TokenRepository;
import com.wanted.gold.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public TokenResponseDto createNewAccessToken(TokenRequestDto requestDto) {
        // 액세스 토큰으로 회원 찾기
        User user = findUserByToken(requestDto);
        // 회원의 정보, 리프레시 토큰 값과 매치되는 리프레시 토큰을 DB에서 가져오기
        Token storedToken = tokenRepository.findByRefreshTokenAndUser_UserId(requestDto.refreshToken(), user.getUserId())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_OR_EXPIRED_TOKEN));
        // 리프레시 토큰 만료됐을 경우
        if(!tokenProvider.validToken(storedToken.getRefreshToken()))
            throw new UnauthorizedException(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
        // 액세스 토큰 재발급
        String newAccessToken = tokenProvider.createAccessToken(user.getUsername());
        // 액세스 토큰 반환
        return new TokenResponseDto(newAccessToken);
    }

    public User findUserByToken(TokenRequestDto requestDto) {
        // 액세스 토큰에서 인증 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(requestDto.accessToken());
        // authentication 에서 인증된 사용자 정보 가져오기
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        // userDetail에 있는 정보 중 username 가져오기
        String username = userDetail.getUsername();
        // 회원 객체 반환
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
