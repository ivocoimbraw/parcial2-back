package com.example.demo.security.jwt;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.user.dto.UserView;
import com.example.demo.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.expiration-hours}") long hours) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.expirationMillis = Duration.ofHours(hours).toMillis();
    }

    public String generateToken(UserView user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("id", user.getId()); 
        return generateToken(user.getEmail(), claims);
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(now.getTime() + expirationMillis))
                .signWith(secretKey)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> extractor) {
        Claims claims = parseClaims(token);
        return extractor.apply(claims);
    }

    public String getSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        try {
            return getExpiration(token).before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    public boolean validateToken(String token, User user) {
        String subject = getSubject(token);
        return subject.equals(user.getUsername()) && !isTokenExpired(token);
    }

    public boolean validateToken(String token, String username) {
        String subject = getSubject(token);
        return subject.equals(username) && !isTokenExpired(token);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
