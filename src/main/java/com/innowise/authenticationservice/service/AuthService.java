package com.innowise.authenticationservice.service;


import com.innowise.authenticationservice.dto.request.LoginRequest;
import com.innowise.authenticationservice.dto.request.RegisterRequest;
import com.innowise.authenticationservice.dto.response.TokenResponseDto;
import com.innowise.authenticationservice.dto.response.TokenValidResponseDto;

public interface AuthService {

    Long register(RegisterRequest registerRequest);

    TokenResponseDto login(LoginRequest loginRequest);

    TokenValidResponseDto validate(String token);

    TokenResponseDto refresh(String token);
}
