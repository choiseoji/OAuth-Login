package com.example.demo.global.jwt;

import com.example.demo.domain.member.enumerate.MemberRole;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private SecretKey secretKey;

    @Value("${spring.jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String generateToken(String category, String identifier, MemberRole role, Long ExpirationTime) {

        return Jwts.builder()
                .claim("category", category)  // access, refresh
                .claim("identifier", identifier)
                .claim("role", role)  // USER, ADMIN
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ExpirationTime))
                .signWith(secretKey)
                .compact();
    }
}
