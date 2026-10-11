package com.user.authentication.service.exception;

public class JwtInvalidExpiredException extends RuntimeException{

    public JwtInvalidExpiredException(String message) {
        super(message);
    }
}
