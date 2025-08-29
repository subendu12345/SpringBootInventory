package com.prod.GreenValley.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.Entities.Role;
import com.prod.GreenValley.repository.RoleRepo;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role findOrCreateRole(String roleName) {
        Optional<Role> roleOptional = roleRepo.findByName(roleName);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        } else {
            Role newRole = new Role(roleName);
            return roleRepo.save(newRole);
        }
    }

    public Optional<Role> findByName(String roleName){
        return roleRepo.findByName(roleName);
    }
    
}
