package com.innowise.authenticationservice.controller;

import com.innowise.authenticationservice.dto.request.AuthRequest;
import com.innowise.authenticationservice.dto.request.TokenRequest;
import com.innowise.authenticationservice.dto.response.TokenResponseDto;
import com.innowise.authenticationservice.dto.response.TokenValidResponseDto;
import com.innowise.authenticationservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AuthRequest authRequest){
        authService.register(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("validate")
    public ResponseEntity<TokenValidResponseDto> validate(@Valid @RequestBody TokenRequest tokenRequest){
        return ResponseEntity.ok(authService.validate(tokenRequest.token()));
    }

    @PostMapping("refresh")
    public ResponseEntity<TokenResponseDto> refresh(@Valid @RequestBody TokenRequest tokenRequest){
        return ResponseEntity.ok(authService.refresh(tokenRequest.token()));
    }

}
