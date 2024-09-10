package com.wanted.gold.exception;

import com.google.api.Http;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 기본
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 회원가입
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 username입니다."),
    USERNAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "6자리 이상 20자리 이하로 설정해주세요."),
    USERNAME_INVALID_CHARACTER(HttpStatus.BAD_REQUEST, "영문 소문자, 숫자, 언더바(_)만 사용할 수 있습니다."),
    USERNAME_CANNOT_ONLY_NUMBER(HttpStatus.BAD_REQUEST, "숫자로만 이루어질 수 없습니다."),
    PASSWORD_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "10자리 이상으로 설정해주세요."),
    PASSWORD_INVALID_CHARACTER(HttpStatus.BAD_REQUEST, "소문자, 숫자, 특수문자를 모두 포함해야 합니다. 포함할 수 있는 특수문자는 !@#$%^&* 입니다."),

    // 로그인
    INVALID_LOGIN_PARAMETER(HttpStatus.BAD_REQUEST, "username 또는 password가 빈 값인지 확인해주세요."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "username 또는 password를 확인해주세요. 회원가입하지 않았을 경우 회원가입을 진행해주세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    INVALID_OR_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다. 다시 로그인해주세요.");

    private final HttpStatus status;
    private final String message;
}
