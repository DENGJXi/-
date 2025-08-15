package com.project.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expireMs;

    public JwtUtil(
            @Value("${security.jwt.secret}") String secretB64,
            @Value("${security.jwt.expire-ms:3600000}") long expireMs
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretB64));
        this.expireMs = expireMs;
    }

    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) throws ExpiredJwtException, JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(30)  // 可选：容忍 30s 时钟漂移
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
