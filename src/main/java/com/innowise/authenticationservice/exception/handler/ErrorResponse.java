package com.innowise.authenticationservice.exception.handler;

import java.time.Instant;

public record ErrorResponse(int status,
                            String message,
                            Instant time) {
}
