package com.innowise.authenticationservice.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityLogin) {
        super(entityLogin + " with this id not found");
    }
}
