package com.example.test.service;

import com.example.test.data.dto.UserDTO;
import com.example.test.data.entity.User;
import com.example.test.data.entity.User.UserRole;

public interface UserService {

  UserDTO getUser(Long id);

  void addUser(UserDTO userDTO);

  default UserDTO toDTO(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .role(user.getRole())
        .build();
  }

  default User toEntity(UserDTO userDTO) {
    return User.builder()
        .id(userDTO.getId())
        .username(userDTO.getUsername())
        .password(userDTO.getPassword())
        .role(userDTO.getRole() != null ? userDTO.getRole() : User.UserRole.USER)
        .build();
  }
}
