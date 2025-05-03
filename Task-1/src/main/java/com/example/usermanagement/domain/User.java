package com.example.usermanagement.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class User {
    private final UUID id;
    private String name;
    private String email;
    private Set<Role> roles = new HashSet<>(); // Initialize to avoid NullPointerException

    // Constructor for creating a new User
    public User(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        // Basic validation (more robust validation should happen in application/infrastructure layer)
        if (name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be blank");
        }
        // Basic email format check (a simple one)
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    // Constructor for reconstructing User from persistence
    public User(UUID id, String name, String email, Set<Role> roles) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.roles = Objects.requireNonNullElseGet(roles, HashSet::new); 
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roles); // Return a copy to maintain encapsulation
    }

    // Business logic
    public void assignRole(Role role) {
        if (role != null) {
            this.roles.add(role);
        }
    }

    public void removeRole(Role role) {
         if (role != null) {
            this.roles.remove(role);
        }
    }

    // Setters for fields that can be updated (if needed)
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be null or blank");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) { // Basic check
             throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    // hashCode and equals based on ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", roles=" + roles.stream().map(Role::getRoleName).toList() + // Show role names for clarity
               '}';
    }
} 