package com.example.usermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, UUID> {
    Optional<RoleJpaEntity> findByRoleName(String roleName);
} 