package com.example.jwt.controller;

import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String home(){
        return "home !";
    }

    @GetMapping("user")
    public String user(){
        return "user !";
    }

    @GetMapping("manager")
    public String manager(){
        return "manager !";
    }

    @GetMapping("admin")
    public String admin(){
        return "admin !";
    }

    @GetMapping("join")
    public String join(){
        User user = new User(
                "yanglet",
                "2028",
                "ROLE_USER"
        );
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return "회원가입완료";
    }
}
