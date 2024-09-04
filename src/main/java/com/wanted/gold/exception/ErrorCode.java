package com.wanted.gold.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 기본
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 로그인
    INVALID_LOGIN_PARAMETER(HttpStatus.BAD_REQUEST, "username 또는 password가 빈 값인지 확인해주세요."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "username 또는 password를 확인해주세요. 회원가입하지 않았을 경우 회원가입을 진행해주세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");


    private final HttpStatus status;
    private final String message;
}
