package com.example.shopdev.repository;

import com.example.shopdev.constants.RoleName;
import com.example.shopdev.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(RoleName name);
}
