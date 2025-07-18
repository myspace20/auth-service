package com.bytebites.auth_service.exceptions;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        String errorMessage,
        int statusCode
)
{
}
