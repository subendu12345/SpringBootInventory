package com.prod.GreenValley.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.GreenValley.Entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
}
