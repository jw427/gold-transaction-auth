package com.wanted.gold.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "회원가입 요청 DTO")
public record SignUpRequestDto(
        @NotBlank @Size(max = 20, message = "최소 6자리 이상으로 설정해주세요.") @Schema(description = "계정명", example = "ijiwon_1234") String username,
        @NotBlank @Size(max = 200, message = "최소 10자리 이상으로 설정해주세요.") @Schema(description = "비밀번호", example = "ijiwon1234!") String password
) {
}
