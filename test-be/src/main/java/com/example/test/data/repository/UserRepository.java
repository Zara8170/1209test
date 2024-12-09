package com.example.test.data.repository;

import com.example.test.data.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {

  @Query("select u from User u where u.username= :username")
  Optional<User> findByUsername(String username);
}
