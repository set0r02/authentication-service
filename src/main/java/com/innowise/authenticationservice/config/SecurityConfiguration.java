package com.innowise.authenticationservice.config;

import com.innowise.authenticationservice.model.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**")
                        .permitAll()
                        .requestMatchers("/admin/**")
                        .hasRole(Role.ADMIN.getAuthority())
                        .requestMatchers("/user/**")
                        .hasAnyRole(Role.ADMIN.getAuthority(),Role.USER.getAuthority())
                        .anyRequest()
                        .authenticated()
                );
        return httpSecurity.build();
    }

}
