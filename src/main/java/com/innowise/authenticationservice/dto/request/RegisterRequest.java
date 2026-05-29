package com.innowise.authenticationservice.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequest(

        @NotBlank(message = "Login should not be blank")
        @Size(min = 3, max = 30, message = "Login should be between {min} and {max} characters")
        String login,
        @NotBlank(message = "Password should not be blank")
        @Size(min = 8, max = 60, message = "Password should be between {min} and {max} characters")
        String password,

        @NotBlank(message = "Name should not be blank")
        @Size(min = 1, max = 255, message = "Name should be between {min} and {max} characters")
        String name,
        @NotBlank(message = "Surname should not be blank")
        @Size(min = 1, max = 255, message = "Surname should be between {min} and {max} characters")
        String surname,
        @NotBlank(message = "Birth date should not be blank")
        @Past(message = "Birth date should not be in the past")
        LocalDate birthDate,
        @NotNull(message = "Email should not be blank")
        @Email(message = "Invalid email")
        String email
) {
}
