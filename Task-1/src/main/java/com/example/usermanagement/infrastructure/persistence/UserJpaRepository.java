package com.example.usermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository // Spring annotation for repository bean detection
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    // Spring Data JPA automatically generates implementation for findBy<AttributeName>
    Optional<UserJpaEntity> findByEmail(String email);

    // You can add custom queries here using @Query annotation if needed
} 