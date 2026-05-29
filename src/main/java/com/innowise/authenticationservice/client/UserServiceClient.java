package com.innowise.authenticationservice.client;

import com.innowise.authenticationservice.dto.request.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user_service",url = "${service.user-service.url}")
public class UserServiceClient {

}
