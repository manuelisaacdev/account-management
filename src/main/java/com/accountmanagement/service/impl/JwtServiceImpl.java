package com.accountmanagement.service.impl;

import com.accountmanagement.config.JWTProperties;
import com.accountmanagement.exception.AuthorizationException;
import com.accountmanagement.model.Role;
import com.accountmanagement.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${spring.application.name}")
    private String springApplicationName;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JWTProperties jwtProperties;

    @Override
    public String generateToken(JWTPayload jwtPayload) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .issuer(springApplicationName)
        .expiresAt(now.plusMillis(jwtPayload.tokenType().equals(TokenType.ACCESS_TOKEN)
        ? jwtProperties.getExpirationAccessToken()
        : jwtProperties.getExpirationRefreshToken()))
        .claim(JwtService.JWT_PAYLOAD_ROLE_KEY, jwtPayload.role())
        .claim(JwtService.JWT_PAYLOAD_USER_ID_KEY, jwtPayload.userId())
        .claim(JwtService.JWT_PAYLOAD_TOKEN_TYPE_KEY, jwtPayload.tokenType())
        .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public JWTPayload getJWTPayload(String authorization)  throws AuthorizationException {
        if (Objects.isNull(authorization) || !authorization.startsWith(JwtService.BEARER_PREFIX)) {
            throw new AuthorizationException("Invalid authorization header");
        }

        Jwt jwt = jwtDecoder.decode(authorization.substring(JwtService.BEARER_PREFIX.length()));
        return new JWTPayload(
        jwt.getClaim(JwtService.JWT_PAYLOAD_USER_ID_KEY),
        Role.valueOf(jwt.getClaim(JwtService.JWT_PAYLOAD_ROLE_KEY)),
        TokenType.valueOf(jwt.getClaim(JwtService.JWT_PAYLOAD_TOKEN_TYPE_KEY)));
    }
}
