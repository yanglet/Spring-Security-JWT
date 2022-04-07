package com.example.jwt.security.jwt.exception;

import com.example.jwt.security.exception.ForbiddenException;

public class TokenHasExpiredException extends ForbiddenException {

    public TokenHasExpiredException(){
        super("Token has Expired");
    }
}
