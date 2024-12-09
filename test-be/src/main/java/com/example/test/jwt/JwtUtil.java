package com.example.test.jwt;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private SecretKey secretKey;

  public JwtUtil(@Value("${authen.jwt.secret.key}") String secretKey) {
    this.secretKey = new SecretKeySpec(secretKey.getBytes(),
        Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getCategory(String token) {
    return Jwts.parser().verifyWith(this.secretKey).build()
        .parseSignedClaims(token).getPayload()
        .get("category", String.class);
  }

  public String getUsername(String token) {
    return Jwts.parser().verifyWith(this.secretKey).build()
        .parseSignedClaims(token).getPayload()
        .get("username", String.class);
  }

  public String getRole(String token) {
    return Jwts.parser().verifyWith(this.secretKey).build()
        .parseSignedClaims(token).getPayload()
        .get("role", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parser().verifyWith(this.secretKey).build()
        .parseSignedClaims(token).getPayload()
        .getExpiration().before(new Date());
  }

  public String CreateJWT(String category, String username, String role, Long expireMs) {
    return Jwts.builder()
        .claim("category", category)
        .claim("username", username)
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expireMs))
        .signWith(this.secretKey)
        .compact();
  }
}
