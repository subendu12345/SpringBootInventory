package com.prod.GreenValley.dataLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.prod.GreenValley.Entities.Role;

import com.prod.GreenValley.service.RoleService;
import com.prod.GreenValley.service.UserService;

import java.util.Set;

@Component
// CommandLineRunner runs the code in its run() method after the application context is loaded.
// This is great for initializing data for demonstration.
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private  RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        Role adminRole = roleService.findOrCreateRole("ADMIN");
        Role staffRole = roleService.findOrCreateRole("STAFF");

        // Create an ADMIN user if they don't exist
        userService.findOrCreateUser("surya@greenvalley.admin", "sumansuriti@2025_admin", Set.of(adminRole, staffRole));

        // Create a STAFF user if they don't exist
        userService.findOrCreateUser("biswajit@greenvalley.staff", "biswajit@2025", Set.of(staffRole));
    }


}