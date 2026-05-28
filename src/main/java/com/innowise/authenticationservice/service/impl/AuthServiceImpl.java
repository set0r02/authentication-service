package com.innowise.authenticationservice.service.impl;

import com.innowise.authenticationservice.dto.request.AuthRequest;
import com.innowise.authenticationservice.dto.response.TokenResponseDto;
import com.innowise.authenticationservice.dto.response.TokenValidResponseDto;
import com.innowise.authenticationservice.exception.AuthenticationException;
import com.innowise.authenticationservice.mapper.AuthMapper;
import com.innowise.authenticationservice.model.Role;
import com.innowise.authenticationservice.model.User;
import com.innowise.authenticationservice.repository.AuthRepository;
import com.innowise.authenticationservice.security.JwtManager;
import com.innowise.authenticationservice.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtManager jwtManager;
    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long register(AuthRequest authRequest) {
        if(authRepository.findByLogin(authRequest.login()).isPresent()){
            throw new AuthenticationException("This " + authRequest.login() + " is already used");
        }
        User user = authMapper.toEntity(authRequest);
        user.setPassword(passwordEncoder.encode(authRequest.password()));
        user.setRole(Role.ROLE_USER);

        authRepository.save(user);
        return user.getId();
    }

    public TokenResponseDto login(AuthRequest authRequest){
        User user = authRepository.findByLogin(authRequest.login()).orElseThrow(
                () -> new AuthenticationException("User with this login: " + authRequest.login() + " is not registered")
        );

        if(!passwordEncoder.matches(authRequest.password(),user.getPassword())){
            throw new AuthenticationException("Password is wrong");
        }

        String accessToken = jwtManager.generateAccessToken(
                user.getId(),user.getRole().getAuthority());
        String refreshToken = jwtManager.generateRefreshToken(
                user.getId());

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

        User user = authRepository.findById(id).orElseThrow(
                () -> new AuthenticationException("User not found")
        );
        String accessTokenRefreshed = jwtManager.generateAccessToken(id,user.getRole().getAuthority());
        String refreshTokenRefreshed = jwtManager.generateRefreshToken(id);
        return new TokenResponseDto(accessTokenRefreshed,refreshTokenRefreshed);

    }

}
