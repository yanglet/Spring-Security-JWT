package com.example.jwt.security.jwt.service;

import com.example.jwt.domain.model.User;
import com.example.jwt.security.auth.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.accessToken.validity}")
    private Long JWT_ACCESS_TOKEN_VALIDITY;

    @Value("${jwt.refreshToken.validity}")
    private Long JWT_REFRESH_TOKEN_VALIDITY;

    @PostConstruct
    protected void init(){
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Map<String, Object> claimMap){
        log.info("JwtService.generateAccessToken()");
        Date now = new Date();
        Date expireTime = new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject("AccessToken")
                .setClaims(claimMap)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateAccessTokenBy(User user){
        log.info("JwtService.generateAccessTokenBy(User user)");
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username", user.getUsername());
        return generateAccessToken(claimMap);
    }

    public String generateAccessTokenBy(String refreshToken){
        log.info("JwtService.generateAccessTokenBy(String refreshToken)");
        String username = findUsernameByToken(refreshToken);
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username",username);
        return generateAccessToken(claimMap);
    }

    public String generateRefreshToken(User user){
        log.info("JwtService.generateRefreshToken()");
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username", user.getUsername());
        Date now = new Date();
        Date expireTime = new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject("RefreshToken")
                .setClaims(claimMap)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String resolveAccessToken(HttpServletRequest request){
        log.info("JwtService.resolveAccessToken()");
        log.info("request.getHeader(\"AccessToken\")" + request.getHeader("AccessToken"));
        return request.getHeader("AccessToken");
    }

    public boolean validateToken(String token){
        log.info("JwtService.validateToken()");
        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        log.info("claims" + claims);
        log.info("!claims.getBody().getExpiration().before(new Date()) = " + !claims.getBody().getExpiration().before(new Date()));
        return !claims.getBody().getExpiration().before(new Date());
    }

    public String findUsernameByToken(String token){
        log.info("JwtService.findUsernameByToken()");
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("username").toString();
    }

    public Authentication getAuthentication(String token){
        log.info("JwtService.getAuthentication()");
        UserDetails userDetails = principalDetailsService.loadUserByUsername(findUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
