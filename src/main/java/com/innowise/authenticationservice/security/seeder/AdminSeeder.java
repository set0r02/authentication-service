package com.innowise.authenticationservice.security.seeder;

import com.innowise.authenticationservice.model.AuthUser;
import com.innowise.authenticationservice.model.Role;
import com.innowise.authenticationservice.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.login}")
    private String adminLogin;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args){
        boolean adminExists = authRepository.findAll()
                .stream()
                .anyMatch(u -> u.getRole() == Role.ROLE_ADMIN);

        if (!adminExists) {
            AuthUser admin = new AuthUser();
            admin.setLogin(adminLogin);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ROLE_ADMIN);

            authRepository.save(admin);
        }
    }
}
