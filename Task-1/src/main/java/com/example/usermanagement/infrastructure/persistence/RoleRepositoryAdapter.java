package com.example.usermanagement.infrastructure.persistence;

import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.domain.Role;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component // Mark as a Spring component for injection
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    // Inject the Spring Data JpaRepository
    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = Objects.requireNonNull(roleJpaRepository);
    }

    @Override
    public Role save(Role role) {
        // Convert domain Role to JPA entity
        RoleJpaEntity roleJpaEntity = RoleJpaEntity.fromDomain(role);
        // Save using JpaRepository
        RoleJpaEntity savedEntity = roleJpaRepository.save(roleJpaEntity);
        // Convert back to domain Role before returning
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        // Find using JpaRepository, map result to domain Role
        return roleJpaRepository.findById(id).map(RoleJpaEntity::toDomain);
    }

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        // Find using JpaRepository, map result to domain Role
        return roleJpaRepository.findByRoleName(roleName).map(RoleJpaEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        // Delete using JpaRepository
        roleJpaRepository.deleteById(id);
    }
} 