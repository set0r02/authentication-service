package com.innowise.authenticationservice.config;

import com.innowise.authenticationservice.model.Role;
import com.innowise.authenticationservice.security.JwtManager;
import com.innowise.authenticationservice.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtManager jwtManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**")
                        .permitAll()
                        .requestMatchers("/admin/**")
                        .hasAuthority(Role.ROLE_ADMIN.getAuthority())
                        .requestMatchers("/user/**")
                        .hasAnyAuthority(Role.ROLE_ADMIN.getAuthority(),Role.ROLE_USER.getAuthority())
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(new JwtTokenFilter(jwtManager), UsernamePasswordAuthenticationFilter.class);
        ;
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
