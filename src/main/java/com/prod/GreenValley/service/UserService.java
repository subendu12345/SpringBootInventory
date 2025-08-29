package com.prod.GreenValley.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.prod.GreenValley.DTO.UserDTO;
import com.prod.GreenValley.Entities.Role;
import com.prod.GreenValley.Entities.User;
import com.prod.GreenValley.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired 
    private PasswordEncoder passwordEncoder;


    public UserDTO findOrCreateUser(UserDTO userDto,  Set<Role> roles){
        Optional<User> userOptional = userRepo.findByUsername(userDto.getUsername());
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encode the password!
            user.setRoles(new HashSet<>(roles));
            userRepo.save(user);
        }

        return userDto;
    }


    public void findOrCreateUser(String username, String password, Set<Role> roles) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password)); // Encode the password!
            user.setRoles(new HashSet<>(roles));
            userRepo.save(user);
        }
    }

    public User findByUserName(String username){
        Optional<User> userOptional = userRepo.findByUsername(username);

        return userOptional.get();
        
    }
}
