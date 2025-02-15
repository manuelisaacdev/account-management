package com.accountmanagement.service;


import com.accountmanagement.exception.AuthorizationException;
import com.accountmanagement.model.Role;
import org.springframework.security.core.GrantedAuthority;

public interface JwtService {
    String BEARER_PREFIX = "Bearer ";
    String AUTHORIZATION_HEADER = "authorization";

    String JWT_PAYLOAD_ROLE_KEY = "role";
    String JWT_PAYLOAD_USER_ID_KEY = "userId";
    String JWT_PAYLOAD_TOKEN_TYPE_KEY = "tokenType";

    String generateToken(JWTPayload jwtPayload);
    JWTPayload getJWTPayload(String authorization) throws AuthorizationException;

    enum TokenType implements GrantedAuthority {
        ACCESS_TOKEN, REFRESH_TOKEN;
        public String getAuthority() {
            return this.name();
        }
    }

    record JWTPayload(Long userId, Role role, TokenType tokenType) {

    }
}
