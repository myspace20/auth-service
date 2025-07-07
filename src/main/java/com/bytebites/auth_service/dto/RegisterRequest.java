package com.bytebites.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Size(min = 10,max = 100, message = "Email should contain at 10 and at most 100 characters")
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 10,max = 100, message = "Password should contain at 10 and at most 100 characters")
        String password,
        @NotBlank(message = "Role is required")
        String role
) {
}