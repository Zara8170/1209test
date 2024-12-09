package com.example.test.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authToken = request.getHeader("Authorization");
    if (authToken == null || !authToken.startsWith("Bearer ")) {
      System.out.println("token null");
      filterChain.doFilter(request, response);
      return;
    }

    String token = authToken.substring(7);
    try {
      jwtUtil.isExpired(token);
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
      response.setCharacterEncoding("UTF-8");
      System.out.println("만료된 토큰");
      response.getWriter().write("만료된 토큰입니다.");
    }

    String category = jwtUtil.getCategory(token);
    if(!category.equals("access")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
      response.setCharacterEncoding("UTF-8");
      System.out.println("허용되지 않는 토큰");
      response.getWriter().write("허용되지 않는 토큰입니다.");
    }

    String username = jwtUtil.getUsername(token);
    String role = jwtUtil.getRole(token);

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role));

    User user = new User(username, "", authorities);
    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);

    SecurityContextHolder.getContext().setAuthentication(auth);
    filterChain.doFilter(request, response);

  }


}
