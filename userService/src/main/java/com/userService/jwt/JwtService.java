package com.userService.jwt;

import com.userService.entities.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwtSecretKey}")
    private String jwtKey ;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Users users) {

        return Jwts.builder()
                .subject(users.getEmail())
                .claim("userId" , users.getUserId())
                .claim("ROLE" , users.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSecretKey())
                .compact();
    }
}
