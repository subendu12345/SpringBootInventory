package com.prod.GreenValley.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.UserDTO;
import com.prod.GreenValley.Entities.User;
import com.prod.GreenValley.service.AuthService;

// REMOVED 'abstract' keyword from the class declaration
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    // Login service
    @PostMapping("/signin")
    public User login(@ModelAttribute("login") UserDTO userDTO) {
        System.out.println("hello I am from login----------");
        return authService.login(userDTO.getEmail(), userDTO.getPassword());
    }

    public boolean isAuthenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("====> "+authentication.getName());
        return authentication != null && authentication.isAuthenticated();
    }

    public String getAuthUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}