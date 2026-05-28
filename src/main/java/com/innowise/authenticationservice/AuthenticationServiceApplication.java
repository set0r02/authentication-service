package com.innowise.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
public class AuthenticationServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class,args);
    }
}
