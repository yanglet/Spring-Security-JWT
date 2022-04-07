package com.example.jwt.domain.model;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;

    public UserLoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
