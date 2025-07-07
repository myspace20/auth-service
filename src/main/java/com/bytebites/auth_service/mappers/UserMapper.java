package com.bytebites.auth_service.mappers;


import com.bytebites.auth_service.dto.UserResponse;
import com.bytebites.auth_service.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
