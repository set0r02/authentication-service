package com.innowise.authenticationservice.service.impl;

import com.innowise.authenticationservice.dto.request.AuthRequest;
import com.innowise.authenticationservice.exception.AutheticationException;
import com.innowise.authenticationservice.exception.WrongDataException;
import com.innowise.authenticationservice.mapper.AuthMapper;
import com.innowise.authenticationservice.model.Role;
import com.innowise.authenticationservice.model.User;
import com.innowise.authenticationservice.repository.AuthRepository;
import com.innowise.authenticationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long register(AuthRequest authRequest) {
        if(authRepository.findByLogin(authRequest.login()).isPresent()){
            throw new AutheticationException("This " + authRequest.login() + " is already used");
        }
        User user = authMapper.toEntity(authRequest);
        user.setPassword(passwordEncoder.encode(authRequest.password()));
        user.setRole(Role.USER);
        authRepository.save(user);
        return user.getId();
    }

    public void login(AuthRequest authRequest){
        User user = authRepository.findByLogin(authRequest.login()).orElseThrow(
                () -> new AutheticationException("User with this login: " + authRequest.login() + " is not registered")
        );

        if(!passwordEncoder.matches(authRequest.password(),user.getPassword())){
            throw new WrongDataException("Password is wrong");
        }
    }
}
