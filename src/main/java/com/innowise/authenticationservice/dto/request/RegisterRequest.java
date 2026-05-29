package com.innowise.authenticationservice.dto.request;

import java.time.LocalDate;

public record RegisterRequest(String login,
                              String password,

                              String name,
                              String surname,
                              LocalDate birthDate,
                              String email
) {
}
