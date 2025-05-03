package com.example.usermanagement.application;

import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.application.interfaces.UserRepository;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import jakarta.transaction.Transactional; // Use jakarta transaction

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// Note: No Spring annotations here (@Service). Configuration happens elsewhere.
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // Needed for assignRole

    // Dependencies are injected via constructor
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.roleRepository = Objects.requireNonNull(roleRepository);
    }

    @Transactional // Keep transactions at the use case boundary
    public User createUser(String name, String email) {
        // Consider adding validation or checks here (e.g., email uniqueness)
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("User with email " + email + " already exists.");
        });

        User user = new User(name, email); // Domain object creation
        return userRepository.save(user); // Persist via repository interface
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User assignRoleToUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));

        user.assignRole(role); // Domain logic
        return userRepository.save(user); // Persist changes
    }

    // Custom Exception classes for better error handling
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
     public static class RoleNotFoundException extends RuntimeException {
        public RoleNotFoundException(String message) {
            super(message);
        }
    }
} 