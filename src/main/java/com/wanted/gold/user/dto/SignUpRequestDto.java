package com.wanted.gold.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
        @NotBlank @Size(max = 20, message = "최소 6자리 이상으로 설정해주세요.") String username,
        @NotBlank @Size(max = 200, message = "최소 10자리 이상으로 설정해주세요.") String password
) {
}
