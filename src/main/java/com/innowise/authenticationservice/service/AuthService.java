package com.innowise.authenticationservice.service;


import com.innowise.authenticationservice.dto.request.AuthRequest;

public interface AuthService {

    Long register(AuthRequest authRequest);
}
