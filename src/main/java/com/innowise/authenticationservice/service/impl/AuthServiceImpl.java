package com.innowise.authenticationservice.service.impl;

import com.innowise.authenticationservice.dto.request.LoginRequest;
import com.innowise.authenticationservice.dto.request.RegisterRequest;
import com.innowise.authenticationservice.dto.response.TokenResponseDto;
import com.innowise.authenticationservice.dto.response.TokenValidResponseDto;
import com.innowise.authenticationservice.exception.AuthenticationException;
import com.innowise.authenticationservice.mapper.AuthMapper;
import com.innowise.authenticationservice.model.Role;
import com.innowise.authenticationservice.model.AuthUser;
import com.innowise.authenticationservice.repository.AuthRepository;
import com.innowise.authenticationservice.security.JwtManager;
import com.innowise.authenticationservice.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtManager jwtManager;
    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long register(RegisterRequest registerRequest) {
        if(authRepository.findByLogin(registerRequest.login()).isPresent()){
            throw new AuthenticationException("This " + registerRequest.login() + " is already used");
        }

        AuthUser authUser = authMapper.toEntity(registerRequest);
        authUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        authUser.setRole(Role.ROLE_USER);

        authRepository.save(authUser);
        return authUser.getId();
    }

    public TokenResponseDto login(LoginRequest loginRequest){
        AuthUser authUser = authRepository.findByLogin(loginRequest.login()).orElseThrow(
                () -> new AuthenticationException("User with this login: " + loginRequest.login() + " is not registered")
        );

        if(!passwordEncoder.matches(loginRequest.password(), authUser.getPassword())){
            throw new AuthenticationException("Password is wrong");
        }

        String accessToken = jwtManager.generateAccessToken(
                authUser.getId(), authUser.getRole().getAuthority());
        String refreshToken = jwtManager.generateRefreshToken(
                authUser.getId());

        return new TokenResponseDto(accessToken,refreshToken);
    }

    public TokenValidResponseDto validate(String token){
        try{
            Claims claims = jwtManager.validateToken(token);
            return new TokenValidResponseDto(
                    true,
                    Long.valueOf(claims.getSubject()),
                    claims.get("role",String.class)
            );
        }catch (Exception exception){
            return new TokenValidResponseDto(false,null,null);
        }
    }

    public TokenResponseDto refresh(String token){
        Claims claims = jwtManager.validateToken(token);
        Long id = Long.valueOf(claims.getSubject());

        AuthUser authUser = authRepository.findById(id).orElseThrow(
                () -> new AuthenticationException("User not found")
        );
        String accessTokenRefreshed = jwtManager.generateAccessToken(id, authUser.getRole().getAuthority());
        String refreshTokenRefreshed = jwtManager.generateRefreshToken(id);
        return new TokenResponseDto(accessTokenRefreshed,refreshTokenRefreshed);

    }

}
