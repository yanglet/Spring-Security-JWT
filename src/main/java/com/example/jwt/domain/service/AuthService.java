package com.example.jwt.domain.service;

import com.example.jwt.domain.exception.PasswordMismatchException;
import com.example.jwt.domain.exception.UserNotFoundException;
import com.example.jwt.domain.repository.UserRepository;
import com.example.jwt.domain.model.User;
import com.example.jwt.domain.model.UserLoginRequest;
import com.example.jwt.domain.model.UserLoginResponse;
import com.example.jwt.security.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserLoginResponse login(UserLoginRequest userLoginRequest, HttpServletResponse response){
        User user = userRepository.findByUsername(userLoginRequest.getUsername()).orElseThrow(UserNotFoundException::new);
        checkPassword(userLoginRequest.getPassword(), user.getPassword());
        String accessToken = jwtService.generateAccessTokenBy(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        response.addHeader("AccessToken", accessToken);
        response.addHeader("RefreshToken", refreshToken);

        return new UserLoginResponse(accessToken, refreshToken);
    }

    private void checkPassword(String loginPassword, String password) {
        if( !bCryptPasswordEncoder.matches(loginPassword, password) ){
            throw new PasswordMismatchException();
        }
    }

    public String getAccessTokenBy(String refreshToken){
        String accessToken = jwtService.generateAccessTokenBy(refreshToken);
        return accessToken;
    }
}
