package com.example.jwt.domain.model;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
