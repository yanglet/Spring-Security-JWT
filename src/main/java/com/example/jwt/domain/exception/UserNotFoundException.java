package com.example.jwt.domain.exception;

import com.example.jwt.security.exception.NotFoundException;

import java.util.function.Supplier;

public class UserNotFoundException extends NotFoundException{
    public UserNotFoundException() {
        super("PassWord is Incorrect");
    }
}
