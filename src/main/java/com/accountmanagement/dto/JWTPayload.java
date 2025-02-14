package com.accountmanagement.dto;

import com.accountmanagement.model.Role;
import com.accountmanagement.service.JwtService.TokenType;

public record JWTPayload(
        Long id,
        Role role,
        TokenType tokenType
) {
}
