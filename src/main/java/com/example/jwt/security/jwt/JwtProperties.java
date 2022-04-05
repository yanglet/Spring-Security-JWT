package com.example.jwt.security.jwt;

public interface JwtProperties {
    String SECRET = "yanglet";
    int EXPIRATION_TIME = 60000 * 10; // 10분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
