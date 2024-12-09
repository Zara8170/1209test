package com.example.test.controller;

import com.example.test.data.dto.UserDTO;
import com.example.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/id")
  public ResponseEntity<UserDTO> getUser(@RequestParam Long id) {
    return ResponseEntity.ok(userService.getUser(id));
  }

  @PostMapping("join")
  public ResponseEntity<String> joinUser(@RequestBody UserDTO userDTO) {
    String password = passwordEncoder.encode(userDTO.getPassword());
    userDTO.setPassword(password);
    userService.addUser(userDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("가입성공");
  }
}
