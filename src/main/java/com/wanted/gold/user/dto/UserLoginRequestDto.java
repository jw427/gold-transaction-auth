package com.wanted.gold.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "로그인 요청 DTO")
public record UserLoginRequestDto(
        @Schema(description = "계정명", example = "ijiwon_1234") String username,
        @Schema(description = "비밀번호", example = "ijiwon1234!") String password) {
}
