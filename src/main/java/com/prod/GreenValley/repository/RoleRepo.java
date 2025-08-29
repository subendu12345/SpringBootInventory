package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.GreenValley.Entities.Role;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long>{
    Optional<Role> findByName(String name);
}
