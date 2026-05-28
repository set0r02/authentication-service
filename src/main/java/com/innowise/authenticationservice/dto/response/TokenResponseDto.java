package com.innowise.authenticationservice.dto.response;

public record TokenResponseDto(String accessToken,
                               String refreshToken) {
}
