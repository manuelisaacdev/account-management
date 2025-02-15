package com.accountmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthenticationFailureException extends AuthenticationException {
    public AuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
