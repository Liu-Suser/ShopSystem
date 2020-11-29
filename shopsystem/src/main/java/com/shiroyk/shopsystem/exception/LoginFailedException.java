package com.shiroyk.shopsystem.exception;


import org.springframework.security.core.AuthenticationException;

public class LoginFailedException extends AuthenticationException {
    public LoginFailedException(String detail) {
        super(detail);
    }
}
