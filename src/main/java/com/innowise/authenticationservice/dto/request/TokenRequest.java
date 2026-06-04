package com.innowise.authenticationservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(

        @NotBlank(message = "Token should not be blank")
        String token
) {
}
