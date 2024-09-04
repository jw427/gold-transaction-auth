package com.wanted.gold.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record UserLoginResponseDto(UUID userId, String token) {
}
