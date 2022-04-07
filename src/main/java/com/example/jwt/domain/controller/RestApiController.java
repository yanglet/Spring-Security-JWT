package com.example.jwt.domain.controller;

import com.example.jwt.domain.repository.UserRepository;
import com.example.jwt.domain.model.User;
import com.example.jwt.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
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

    @Secured({"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("user")
    public String user(Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principal.getUsername() = " + principal.getUsername());
        return "user !";
    }

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("manager")
    public String manager(){
        return "manager !";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("admin")
    public String admin(){
        return "admin !";
    }

    @GetMapping("join")
    public String join(){
        User user = new User(
                "yanglet1",
                "20281",
                "ROLE_USER"
        );
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return "회원가입완료";
    }
}
