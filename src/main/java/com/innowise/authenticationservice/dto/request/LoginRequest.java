package com.innowise.authenticationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Login should not be blank")
        @Size(min = 3, max = 30, message = "Login should be between {min} and {max} characters")
        String login,

        @NotBlank(message = "Password should not be blank")
        @Size(min = 8, max = 60, message = "Password should be between {min} and {max} characters")
        String password
) {
}
