package com.wanted.gold.user.controller;

import com.wanted.gold.user.dto.SignUpRequestDto;
import com.wanted.gold.user.dto.SignUpResponseDto;
import com.wanted.gold.user.dto.UserLoginRequestDto;
import com.wanted.gold.user.dto.UserLoginResponseDto;
import com.wanted.gold.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 시 사용하는 API\n" +
            "1. 계정명은 6자리 이상 20자리 이하의 영문 소문자, 숫자, 언더바(_)로 구성되어야 합니다. 숫자만 사용할 수 없습니다.\n" +
            "2. 비밀번호는 10자리 이상으로 소문자, 숫자, 특수문자가 모두 포함되어야 합니다. 특수문자는 특수문자는 !@#$%^&*만 가능합니다.")
    @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(schema = @Schema(implementation = SignUpResponseDto.class))})
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = userService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 시 사용하는 API")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(schema = @Schema(implementation = UserLoginResponseDto.class))})
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponseDto responseDto = userService.login(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
}
