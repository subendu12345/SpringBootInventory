package com.prod.GreenValley.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.Entities.User;
import com.prod.GreenValley.repository.UserRepo;

@Service
public class AuthService {
    

    @Autowired
    private UserRepo  userRepo;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    //login service
    public User login(String username, String password) {
        System.out.println("=========================login callingg.... "+ username);
        User user = userRepo.findByUsername(username).orElseThrow();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
}
