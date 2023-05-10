package com.example.backend.Utils;

import com.example.backend.User.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.paulschwarz.springdotenv.DotenvPropertyLoader;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtHelper {
  Dotenv dotenv = Dotenv.load();

   private final String SECRET_KEY = dotenv.get("SECRET_KEY");
  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("user_id", user.getId());
    claims.put("username", user.getUsername());
    claims.put("role", user.getRole());

    return jwtToken(claims, user.getUsername());
  }

  private String jwtToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(subject)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }
}
