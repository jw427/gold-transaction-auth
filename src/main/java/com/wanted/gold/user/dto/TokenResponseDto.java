package com.wanted.gold.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "액세스 토큰 재발급 응답 DTO")
public record TokenResponseDto(
        @Schema(description = "재발급된 액세스 토큰") String accessToken) {
}
