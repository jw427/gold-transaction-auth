package com.wanted.gold.user.service;

import com.wanted.gold.exception.BadRequestException;
import com.wanted.gold.exception.ConflictException;
import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.user.dto.SignUpRequestDto;
import com.wanted.gold.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    // 회원가입 시 유효성 검사
    public void validateRequest(SignUpRequestDto requestDto) {
        // username 유효성 검사
        validateUsername(requestDto.username());
        // username 중복 확인
        if(userRepository.findByUsername(requestDto.username()).isPresent())
            throw new ConflictException(ErrorCode.ALREADY_EXIST_USERNAME);
        // password 유효성 검사
        validatePassword(requestDto.password());
    }

    // username 유효성 검사
    public void validateUsername(String username) {
        // 1. 길이가 6자리 미만이거나 20자리 초과일 경우
        if(username.length() < 6 || username.length() > 20)
            throw new BadRequestException(ErrorCode.USERNAME_LENGTH_INVALID);
        // 2. 영문 소문자, 숫자, 언더바(_) 외 다른 문자가 포함된 경우
        if(!username.matches("^[a-z0-9_]+$"))
            throw new BadRequestException(ErrorCode.USERNAME_INVALID_CHARACTER);
        // 3. 숫자로만 이루어졌을 경우
        if(username.matches("^[0-9]+$"))
            throw new BadRequestException(ErrorCode.USERNAME_CANNOT_ONLY_NUMBER);
    }

    // password 유효성 검사
    public void validatePassword(String password) {
        // 1. 길이가 10자리 미만일 경우
        if(password.length() < 10)
            throw new BadRequestException(ErrorCode.PASSWORD_LENGTH_INVALID);
        // 2. 소문자, 숫자, 특수문자 중 하나라도 포함되지 않은 경우 - 포함되는 특수문자 !@#$%^&*
        if (!password.matches(".*[a-z].*") || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*].*"))
            throw new BadRequestException(ErrorCode.PASSWORD_INVALID_CHARACTER);
    }
}
