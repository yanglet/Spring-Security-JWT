package com.example.jwt.domain.controller;

import com.example.jwt.domain.model.UserLoginRequest;
import com.example.jwt.domain.model.UserLoginResponse;
import com.example.jwt.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest userLoginRequest){
        return authService.login(userLoginRequest);
    }

    @GetMapping("/accessToken")
    public String requestAccessToken(@RequestHeader String refreshToken){
        String accessToken = authService.getAccessTokenBy(refreshToken);
        return accessToken;
    }
}
