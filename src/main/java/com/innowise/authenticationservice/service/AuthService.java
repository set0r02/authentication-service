package com.innowise.authenticationservice.service;


import com.innowise.authenticationservice.dto.request.AuthRequest;
import com.innowise.authenticationservice.dto.response.TokenResponseDto;

public interface AuthService {

    Long register(AuthRequest authRequest);

    TokenResponseDto login(AuthRequest authRequest);
}
