package com.wanted.gold.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "액세스 토큰 재발급 요청 DTO")
public record TokenRequestDto(
        @Schema(description = "기존 액세스 토큰") String accessToken,
        @Schema(description = "리프레시 토큰") String refreshToken) {
}
