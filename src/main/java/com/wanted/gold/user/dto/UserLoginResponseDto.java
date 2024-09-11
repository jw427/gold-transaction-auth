package com.wanted.gold.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(title = "로그인 응답 DTO")
public record UserLoginResponseDto(
        @Schema(description = "회원 식별번호") UUID userId,
        @Schema(description = "액세스 토큰") String accessToken,
        @Schema(description = "리프레시 토큰") String refreshToken) {
}
