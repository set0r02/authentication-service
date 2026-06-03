package com.innowise.authenticationservice.client;

import com.innowise.authenticationservice.client.dto.UserCreatedResponseDto;
import com.innowise.authenticationservice.client.dto.UserInputDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userservice", url = "${spring.service.user-service.url}")
public interface UserServiceClient {

    @PostMapping("/api/users")
    ResponseEntity<UserCreatedResponseDto> createUser(@RequestBody UserInputDto userInputDto);

    ResponseEntity<Void> deleteUser(@PathVariable Long id);

}
