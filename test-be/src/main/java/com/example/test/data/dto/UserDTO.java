package com.example.test.data.dto;

import com.example.test.data.entity.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

  private Long id;
  private String username;
  private String password;
  private UserRole role;
  private String adminCode;
}
