package com.example.accountapp.security.config;

import com.example.accountapp.security.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.*;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.stream.Collectors;


@Component
public class JwtUtil {
    private final String secret = "secret-key";
    private final long expirationTime = 3600000;

    private SecretKey secretKey;
    @PostConstruct
    public void init() {

        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
    public String generateToken(String username, Set<Role> roles) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.stream()
                .map(role -> {
                    Map<String, Object> simpleRole = new HashMap<>();
                    simpleRole.put("name", role.getName());
                    simpleRole.put("createdDate", role.getCreatedDate().toString()); // convert to ISO string
                    return simpleRole;
                }).collect(Collectors.toList())
        )
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 1 day
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return (List<String>) extractClaims(token).get("roles");
    }
}
