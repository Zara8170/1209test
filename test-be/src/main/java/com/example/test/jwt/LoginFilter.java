package com.example.test.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  private final JwtUtil jwtUtil;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    String username = obtainUsername(request);
    String password = obtainPassword(request);

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        username, password, null);

    return authenticationManager.authenticate(authRequest);
  }

  @Override
  public void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException {
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    String username = userDetails.getUsername();

    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority grantedAuthority = iterator.next();
    String role = grantedAuthority.getAuthority();

    if (!role.startsWith("ROLE_")) {
      role = "ROLE_" + role;
    }

    String accessToken = this.jwtUtil.CreateJWT("access", username, role, 60 * 60 * 1000L);
    String refreshToken = this.jwtUtil.CreateJWT("refresh", username, role, 24 * 60 * 60 * 1000L);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonmessage = objectMapper.writeValueAsString(role);

    response.addHeader("Authorization", "Bearer " + accessToken);
    response.addCookie(createCookie("refresh", refreshToken));
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(jsonmessage); // body
  }

  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(24 * 60 * 60);
    return cookie;
  }

  @Override
  public void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
      AuthenticationException failed) throws IOException, ServletException {
    Map<String, String> responseData = new HashMap<>();
    responseData.put("message", "계정정보가 틀립니다.");

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonmessage = objectMapper.writeValueAsString(responseData);

    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    res.getWriter().write(jsonmessage);
  }
}
