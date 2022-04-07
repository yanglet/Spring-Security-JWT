package com.example.jwt.security.jwt.service;

import com.example.jwt.domain.model.User;
import com.example.jwt.security.auth.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
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
        Date now = new Date();
        Date expireTime = new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject("accessToken")
                .setClaims(claimMap)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateAccessTokenBy(User user){
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username", user.getUsername());
        return generateAccessToken(claimMap);
    }

    public String generateAccessTokenBy(String refreshToken){
        String username = findUsernameByToken(refreshToken);
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username",username);
        return generateAccessToken(claimMap);
    }

    public String generateRefreshToken(User user){
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username", user.getUsername());
        Date now = new Date();
        Date expireTime = new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject("refreshToken")
                .setClaims(claimMap)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String resolveAccessToken(HttpServletRequest request){
        return request.getHeader("AccessToken");
    }

    public boolean validateToken(String token){
        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public String findUsernameByToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("usernamer").toString();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = principalDetailsService.loadUserByUsername(findUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
