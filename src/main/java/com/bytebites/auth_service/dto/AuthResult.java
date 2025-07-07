package com.bytebites.auth_service.dto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record AuthResult(
        Authentication authentication,
        List<String> roles
) {
}
