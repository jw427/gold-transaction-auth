package com.wanted.gold.user.service;

import com.wanted.gold.exception.BadRequestException;
import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.user.config.TokenProvider;
import com.wanted.gold.user.domain.Role;
import com.wanted.gold.user.domain.Token;
import com.wanted.gold.user.domain.User;
import com.wanted.gold.user.dto.SignUpRequestDto;
import com.wanted.gold.user.dto.SignUpResponseDto;
import com.wanted.gold.user.dto.UserLoginRequestDto;
import com.wanted.gold.user.dto.UserLoginResponseDto;
import com.wanted.gold.user.repository.TokenRepository;
import com.wanted.gold.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserValidator userValidator;

    // 회원가입
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        // username과 password 검증
        userValidator.validateRequest(requestDto);
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.password());
        // User 생성
        User user = User.builder()
                .username(requestDto.username())
                .password(encodedPassword)
                .role(Role.ROLE_MEMBER)
                .build();
        // db 저장
        userRepository.save(user);
        // SignUpResponseDto 생성 및 반환
        return new SignUpResponseDto("회원가입이 완료되었습니다.", user.getUsername());
    }

    // 로그인
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        // 로그인 시 username이나 password에 빈 값이 있을 경우
        if(requestDto.username() == null || requestDto.username().isBlank() || requestDto.password() == null || requestDto.password().isBlank())
            throw new BadRequestException(ErrorCode.INVALID_LOGIN_PARAMETER);
        // username으로 회원 조회
        User user = userRepository.findByUsername(requestDto.username())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.LOGIN_FAILED));
        // password 일치여부
        if(!passwordEncoder.matches(requestDto.password(), user.getPassword()))
            throw new UnauthorizedException(ErrorCode.LOGIN_FAILED);
        // refreshToken 발급 및 DB 저장
        String refreshToken = tokenProvider.createRefreshToken();
        tokenRepository.save(new Token(refreshToken, user));
        // 회원 인증 후 accessToken 발급
        return new UserLoginResponseDto(user.getUserId(), tokenProvider.createAccessToken(requestDto.username()), refreshToken);
    }
}
