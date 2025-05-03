package com.example.usermanagement.application.interfaces;

import com.example.usermanagement.domain.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    Optional<Role> findByRoleName(String roleName); // Useful for checking duplicates
    void deleteById(UUID id);
    // Add other query methods if needed, e.g., findAll()
} 