package com.accountmanagement.service;


import com.accountmanagement.dto.JWTPayload;
import com.accountmanagement.exception.AuthorizationException;
import org.springframework.security.core.GrantedAuthority;

public interface JwtService {
    String generateToken(JWTPayload jwtPayload);
    JWTPayload getJWTPayload(String authorization) throws AuthorizationException;

    enum TokenType implements GrantedAuthority {
        ACCESS_TOKEN, REFRESH_TOKEN;
        public String getAuthority() {
            return this.name();
        }
    }
}
