package com.bytebites.auth_service.services;

import com.bytebites.auth_service.dto.LoginRequest;
import com.bytebites.auth_service.dto.RegisterRequest;
import com.bytebites.auth_service.dto.AuthResponse;
import com.bytebites.auth_service.models.User;

public interface AuthService {
    User register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
