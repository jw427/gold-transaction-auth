package com.wanted.gold.user.controller;

import com.wanted.gold.user.dto.TokenRequestDto;
import com.wanted.gold.user.dto.TokenResponseDto;
import com.wanted.gold.user.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
@Tag(name = "Token", description = "Token API")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/access-token")
    @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰이 만료됐을 경우 재발급 받는 API")
    @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(schema = @Schema(implementation = TokenResponseDto.class))})
    public ResponseEntity<TokenResponseDto> createNewAccessToken(@RequestBody TokenRequestDto requestDto) {
        TokenResponseDto responseDto = tokenService.createNewAccessToken(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
