package com.innowise.authenticationservice.client;

import com.innowise.authenticationservice.client.dto.UserCreatedResponseDto;
import com.innowise.authenticationservice.client.dto.UserInputDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userservice", url = "${spring.service.user-service.url}")
public interface UserServiceClient {

    @PostMapping("/api/users")
    ResponseEntity<UserCreatedResponseDto> createUser(@Valid @RequestBody UserInputDto userInputDto);

    @DeleteMapping("/api/users/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);

}
