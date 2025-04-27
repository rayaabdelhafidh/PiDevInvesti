package com.example.investi.Services;

import com.example.investi.Entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

  // À stocker dans application.properties
    @Value("${jwt.secret}")
    private String secretKey;


    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Set the subject (user email)
                .claim("authorities", user.getAuthorities().stream()
                        .map(authority -> Map.of("authority", authority.getAuthority())) // Format authorities as [{ "authority": "ROLE_CLIENT" }]
                        .toList())
                .claim("clientId", user.getId()) // Add clientId as a custom claim
                .setIssuedAt(new Date()) // Set the issued-at timestamp
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // Sign with HMAC-SHA256
                .compact(); // Compact the token into a string
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}