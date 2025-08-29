package com.prod.GreenValley.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.Entities.User;
import com.prod.GreenValley.repository.UserRepo;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    public CustomUserDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security calls this method to load user information during login.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User user = userOptional.get();
        // Convert our custom User entity to Spring Security's UserDetails object.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // The stored, hashed password
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .toArray(String[]::new)) // Pass roles as a varargs array
                .build();
    }
}
