package com.example.usermanagement.infrastructure.persistence;

import com.example.usermanagement.application.interfaces.UserRepository;
import com.example.usermanagement.domain.User;
import org.springframework.stereotype.Component; // Use @Component or @Repository

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component // Mark as a Spring component for injection
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    // Inject the Spring Data JpaRepository
    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = Objects.requireNonNull(userJpaRepository);
    }

    @Override
    public User save(User user) {
        // Convert domain User to JPA entity
        UserJpaEntity userJpaEntity = UserJpaEntity.fromDomain(user);
        
        // If assigning roles, ensure RoleJpaEntity instances are managed/retrieved
        // (This might require fetching existing RoleJpaEntities by ID if they are not new)
        // Simplified here assumes roles in the user object are either new or already mapped correctly.

        // Save using JpaRepository
        UserJpaEntity savedEntity = userJpaRepository.save(userJpaEntity);
        
        // Convert back to domain User before returning
        return savedEntity.toDomain();
    }

    @Override
    public Optional<User> findById(UUID id) {
        // Find using JpaRepository, map result to domain User
        return userJpaRepository.findById(id).map(UserJpaEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // Find using JpaRepository, map result to domain User
        return userJpaRepository.findByEmail(email).map(UserJpaEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        // Delete using JpaRepository
        userJpaRepository.deleteById(id);
    }
} 