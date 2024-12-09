package com.example.test.configure;

import com.example.test.component.CustomAccessDeniedHandler;
import com.example.test.component.CustomAuthenticationEntryPoint;
import com.example.test.jwt.JwtFilter;
import com.example.test.jwt.JwtUtil;
import com.example.test.jwt.LoginFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtUtil jwtUtil;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .formLogin(formLogin -> formLogin.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .authorizeHttpRequests(authorize ->
            authorize.requestMatchers("/api/users/join", "/login", "/api/books", "/api/categories",
                    "/reissue")
                .permitAll()
                .anyRequest().authenticated()
        );

    http.cors(cors -> cors.configurationSource(request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.setAllowedOrigins(
          Arrays.asList("http://localhost:3000", "http://localhost:3001")
      );
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      config.addExposedHeader("Authorization");
      return config;
    }));

    http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(new JwtFilter(this.jwtUtil), LoginFilter.class);

    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
        UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(exception -> {
          exception.authenticationEntryPoint(customAuthenticationEntryPoint);
          exception.accessDeniedHandler(customAccessDeniedHandler);
        }
    );

    return http.build();
  }
}
