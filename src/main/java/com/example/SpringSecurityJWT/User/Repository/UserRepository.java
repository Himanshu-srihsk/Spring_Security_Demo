package com.example.SpringSecurityJWT.User.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringSecurityJWT.User.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
  Optional<User> findByEmail(String email);
}
