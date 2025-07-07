package com.bytebites.auth_service.dto;

public record AuthResponse(
        String email,
        String token
) {
}