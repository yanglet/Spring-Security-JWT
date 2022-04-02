package com.example.jwt.config;

import com.example.jwt.security.jwt.JwtAuthenticationFilter;
import com.example.jwt.security.jwt.JwtAuthorizationFilter;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 세션방식 사용x
        http
                .addFilter(corsFilter)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable() // 기본 인증방식 사용x
                // 필터 등록 WebSecurityConfigurerAdapter가 authenticationManager를 들고 있음
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeHttpRequests()
                .antMatchers("/api/v1/user/**").hasRole("ROLE_USER")
                .antMatchers("/api/v1/user/**", "/api/v1/manager/**").hasRole("ROLE_MANGER")
                .antMatchers("/api/v1/user/**", "/api/v1/manager/**", "/api/vi/admin/**").hasRole("ROLE_ADMIN")
                .anyRequest().permitAll();
    }
}
