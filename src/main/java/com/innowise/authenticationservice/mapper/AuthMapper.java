package com.innowise.authenticationservice.mapper;

import com.innowise.authenticationservice.dto.AuthRequest;
import com.innowise.authenticationservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(AuthRequest authRequest);

}
