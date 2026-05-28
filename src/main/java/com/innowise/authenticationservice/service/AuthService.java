package com.innowise.authenticationservice.service;


import com.innowise.authenticationservice.dto.request.AuthRequest;
import com.innowise.authenticationservice.dto.response.TokenResponseDto;
import com.innowise.authenticationservice.dto.response.TokenValidResponseDto;

public interface AuthService {

    Long register(AuthRequest authRequest);

    TokenResponseDto login(AuthRequest authRequest);

    TokenValidResponseDto validate(String token);

    TokenResponseDto refresh(String token);
}
