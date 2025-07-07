package com.bytebites.auth_service.mappers;


import com.bytebites.auth_service.dto.AuthResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthResponse toAuthResponse(AuthResponse authResponse);
}
