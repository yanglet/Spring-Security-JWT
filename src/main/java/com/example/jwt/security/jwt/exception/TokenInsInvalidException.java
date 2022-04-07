package com.example.jwt.security.jwt.exception;

import com.example.jwt.security.exception.ForbiddenException;

public class TokenInsInvalidException extends ForbiddenException {
public TokenInsInvalidException() {
    super("Token is invalid");
}
}
