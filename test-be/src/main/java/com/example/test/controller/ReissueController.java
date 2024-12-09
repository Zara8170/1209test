package com.example.test.controller;

import com.example.test.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

  private final JwtUtil jwtUtil;

  @PostMapping(value = "/reissue")
  public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
    String refresh = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
        break;
      }
    }

    if(refresh == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("refresh 토큰 null"); // 400
    }

    try {
      this.jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh 토큰 유효기간 만료"); // 401
    }

    String category = jwtUtil.getCategory(refresh);
    if(!category.equals("refresh")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 refresh 토큰"); // 400
    }

    String username = jwtUtil.getUsername(refresh);
    String role = jwtUtil.getRole(refresh);

    String newAccessToken = jwtUtil.CreateJWT("access", username, role, 60*60*1000L);
    response.addHeader("Authorization", "Bearer " + newAccessToken);
    response.setCharacterEncoding("UTF-8");
    return ResponseEntity.status(HttpStatus.OK).body("새 토큰 발급 성공");

  }
}
