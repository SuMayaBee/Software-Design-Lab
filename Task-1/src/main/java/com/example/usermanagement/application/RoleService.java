package com.example.usermanagement.application;

import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.domain.Role;
import jakarta.transaction.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// Note: No Spring annotations here (@Service). Configuration happens elsewhere.
public class RoleService {

    private final RoleRepository roleRepository;

    // Dependency injected via constructor
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = Objects.requireNonNull(roleRepository);
    }

    @Transactional
    public Role createRole(String roleName) {
        // Check if role name already exists
        roleRepository.findByRoleName(roleName).ifPresent(r -> {
            throw new IllegalArgumentException("Role with name " + roleName + " already exists.");
        });

        Role role = new Role(roleName); // Domain object creation
        return roleRepository.save(role); // Persist via repository interface
    }

    public Optional<Role> getRoleById(UUID id) {
        return roleRepository.findById(id);
    }

    // Add other role-related use cases if needed (e.g., deleteRole, updateRole)
} 