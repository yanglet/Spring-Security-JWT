package com.example.jwt.security.jwt;

import org.springframework.beans.factory.annotation.Value;

public interface JwtProperties {
    String SECRET = "";
    int EXPIRATION_TIME = 60000 * 10; // 10분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
