package com.accountmanagement.service.impl;

import com.accountmanagement.config.JWTProperties;
import com.accountmanagement.dto.JWTPayload;
import com.accountmanagement.exception.AuthorizationException;
import com.accountmanagement.model.Role;
import com.accountmanagement.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JWTProperties jwtProperties;
    public static final String BEARER = "Bearer ";

    @Override
    public String generateToken(JWTPayload jwtPayload) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .issuer("account-management")
        .expiresAt(now.plusMillis(jwtPayload.tokenType().equals(TokenType.ACCESS_TOKEN)
        ? jwtProperties.getExpirationAccessToken()
        : jwtProperties.getExpirationRefreshToken()))
        .claim("userId", jwtPayload.id())
        .claim("role", jwtPayload.role())
        .claim("tokenType", jwtPayload.tokenType())
        .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public JWTPayload getJWTPayload(String authorization)  throws AuthorizationException {
        if (Objects.isNull(authorization) || !authorization.startsWith(BEARER)) {
            throw new AuthorizationException("Invalid authorization header");
        }

        Jwt jwt = jwtDecoder.decode(authorization.substring(BEARER.length()));
        Role role = jwt.getClaim("role");
        Long userId = jwt.getClaim("userId");
        TokenType tokenType = jwt.getClaim("tokenType");
        return new JWTPayload(userId, role, tokenType);
    }
}
