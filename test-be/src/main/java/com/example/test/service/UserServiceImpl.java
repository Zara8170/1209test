package com.example.test.service;

import com.example.test.data.dto.UserDTO;
import com.example.test.data.entity.User;
import com.example.test.data.entity.User.UserRole;
import com.example.test.data.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

  private static final String ADMIN_CODE = "1234";

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDTO getUser(Long id) {
    User user = userRepository.findById(id).orElse(null);
    return user != null ? toDTO(user) : null;
  }

  @Transactional
  @Override
  public void addUser(UserDTO userDTO) {
    User user = toEntity(userDTO);

    if(ADMIN_CODE.equals(userDTO.getAdminCode())) {
      user.setRole(UserRole.ADMIN);
    } else {
      user.setRole(UserRole.USER);
    }

    User saveUser = userRepository.save(user);
    toDTO(saveUser);
  }

  @Transactional
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User " + username + " not found");
    }
    User user1 = user.get();

    List<GrantedAuthority> authorities = new ArrayList<>();
    if (user1.getRole() == UserRole.ADMIN) {
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    } else {
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    return new org.springframework.security.core.userdetails.User(user1.getUsername()
        , user1.getPassword(), authorities);
  }
}



