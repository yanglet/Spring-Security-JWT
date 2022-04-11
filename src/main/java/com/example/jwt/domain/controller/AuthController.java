package com.example.jwt.domain.controller;

import com.example.jwt.domain.model.UserLoginRequest;
import com.example.jwt.domain.model.UserLoginResponse;
import com.example.jwt.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest userLoginRequest,
                                   HttpServletResponse response){
        return authService.login(userLoginRequest, response);
    }

    @GetMapping("/accessToken")
    public String requestAccessToken(@RequestHeader String refreshToken){
        String accessToken = authService.getAccessTokenBy(refreshToken);
        return accessToken;
    }
}
