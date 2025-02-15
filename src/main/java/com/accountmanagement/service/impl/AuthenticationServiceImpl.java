package com.accountmanagement.service.impl;

import com.accountmanagement.dto.AuthenticationResponse;
import com.accountmanagement.service.JwtService.JWTPayload;
import com.accountmanagement.model.User;
import com.accountmanagement.service.AuthenticationService;
import com.accountmanagement.service.JwtService;
import com.accountmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public AuthenticationResponse authenticate(User user) {
        return new AuthenticationResponse(user,
        jwtService.generateToken(new JWTPayload(user.getId(), user.getRole(), JwtService.TokenType.ACCESS_TOKEN)),
        jwtService.generateToken(new JWTPayload(user.getId(), user.getRole(), JwtService.TokenType.REFRESH_TOKEN)));
    }

    @Override
    public AuthenticationResponse refreshToken(String authorization) {
        return this.authenticate(userService.findById(jwtService.getJWTPayload(authorization).userId()));
    }

}
