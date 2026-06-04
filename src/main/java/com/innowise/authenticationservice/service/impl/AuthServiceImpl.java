package com.innowise.authenticationservice.service.impl;

import com.innowise.authenticationservice.client.UserServiceClient;
import com.innowise.authenticationservice.client.dto.UserCreatedResponseDto;
import com.innowise.authenticationservice.client.dto.UserInputDto;
import com.innowise.authenticationservice.dto.request.LoginRequest;
import com.innowise.authenticationservice.dto.request.RegisterRequest;
import com.innowise.authenticationservice.dto.response.TokenResponseDto;
import com.innowise.authenticationservice.dto.response.TokenValidResponseDto;
import com.innowise.authenticationservice.exception.AuthServiceException;
import com.innowise.authenticationservice.mapper.AuthMapper;
import com.innowise.authenticationservice.model.Role;
import com.innowise.authenticationservice.model.AuthUser;
import com.innowise.authenticationservice.repository.AuthRepository;
import com.innowise.authenticationservice.security.JwtManager;
import com.innowise.authenticationservice.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtManager jwtManager;
    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional
    public Long register(RegisterRequest registerRequest) {
        if(authRepository.existsAuthUserByLogin(registerRequest.login())){
            throw new AuthServiceException("This " + registerRequest.login() + " is already used");
        }

        AuthUser authUser = authMapper.toEntity(registerRequest);
        authUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        authUser.setRole(Role.ROLE_USER);

        Long userId = null;

        try {
            UserCreatedResponseDto createdUser =
                    userServiceClient.createUser(
                            new UserInputDto(
                                    registerRequest.name(),
                                    registerRequest.surname(),
                                    registerRequest.birthDate(),
                                    registerRequest.email()
                            )
                    ).getBody();

            if (createdUser == null) {
                throw new AuthServiceException("Failed to create user in UserService");
            }

            userId = createdUser.id();

            AuthUser savedUser = authRepository.save(authUser);
            return savedUser.getId();

        } catch (Exception e) {
            removeExternalUser(userId);
            throw e;
        }
    }

    private void removeExternalUser(Long userId) {
        if (userId == null) {
            return;
        }
        try {
            userServiceClient.deleteUser(userId);
        } catch (Exception ex) {
                log.warn("Failed to delete user with id {} while rolling back registration", userId, ex);
        }

    }

    @Override
    public TokenResponseDto login(LoginRequest loginRequest){
        AuthUser authUser = authRepository.findByLogin(loginRequest.login()).orElseThrow(
                () -> new AuthServiceException("User with this login: " + loginRequest.login() + " is not registered")
        );

        if(!passwordEncoder.matches(loginRequest.password(), authUser.getPassword())){
            throw new AuthServiceException("Password is wrong");
        }

        String accessToken = jwtManager.generateAccessToken(
                authUser.getId(), authUser.getRole().getAuthority());
        String refreshToken = jwtManager.generateRefreshToken(
                authUser.getId());

        return new TokenResponseDto(accessToken,refreshToken);
    }

    @Override
    public TokenValidResponseDto validate(String token){
        try{
            Claims claims = jwtManager.validateToken(token);
            return new TokenValidResponseDto(
                    true,
                    Long.valueOf(claims.getSubject()),
                    claims.get("role",String.class)
            );
        }catch (JwtException jwtException){
            return new TokenValidResponseDto(false,null,null);
        }
    }

    @Override
    public TokenResponseDto refresh(String token){
        Claims claims = jwtManager.validateToken(token);

        String tokenType = claims.get("tokenType",String.class);
        if(!"refresh".equals(tokenType)){
            throw new AuthServiceException("Invalid token type for refresh");
        }
        Long id = Long.valueOf(claims.getSubject());

        AuthUser authUser = authRepository.findById(id).orElseThrow(
                () -> new AuthServiceException("User not found")
        );
        String accessTokenRefreshed = jwtManager.generateAccessToken(id, authUser.getRole().getAuthority());
        String refreshTokenRefreshed = jwtManager.generateRefreshToken(id);
        return new TokenResponseDto(accessTokenRefreshed,refreshTokenRefreshed);

    }

}
