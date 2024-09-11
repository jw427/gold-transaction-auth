package com.wanted.gold.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "회원가입 응답 DTO")
public record SignUpResponseDto(
        @Schema(description = "응답 메시지", example = "회원가입이 완료되었습니다.") String message,
        @Schema(description = "회원가입 계정명", example = "ijiwon_1234") String username) {
}
