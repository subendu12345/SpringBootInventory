package com.prod.GreenValley.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.prod.GreenValley.Entities.User;
import com.prod.GreenValley.service.UserService;

public class CustomUserDetailService  implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailService   =========================>" +username);
        User user = userService.findByUserName(username);
        return new CustomUserDetail(user);
    }
}
