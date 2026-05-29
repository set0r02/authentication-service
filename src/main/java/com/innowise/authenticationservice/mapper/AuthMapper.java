package com.innowise.authenticationservice.mapper;

import com.innowise.authenticationservice.dto.request.RegisterRequest;
import com.innowise.authenticationservice.model.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    AuthUser toEntity(RegisterRequest registerRequest);

}
