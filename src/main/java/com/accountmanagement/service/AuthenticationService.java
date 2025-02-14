package com.accountmanagement.service;


import com.accountmanagement.dto.AuthenticationResponse;
import com.accountmanagement.model.User;

public interface AuthenticationService {
    AuthenticationResponse authenticate(User user);
    AuthenticationResponse refreshToken(String authorization);
}
