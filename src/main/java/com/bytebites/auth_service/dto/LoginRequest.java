package com.bytebites.auth_service.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "Email is required")
        String email,
        @NotNull(message = "Password is required")
        String password
) {
}
