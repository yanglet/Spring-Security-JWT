package com.example.jwt.domain.exception;

import com.example.jwt.security.exception.ForbiddenException;

public class PasswordMismatchException extends ForbiddenException {
    public PasswordMismatchException() {
        super("PassWord is Incorrect");
    }
}
