package com.innowise.authenticationservice.dto.response;

public record TokenValidResponseDto(boolean valid,
                                    Long id,
                                    String role
) {
}
