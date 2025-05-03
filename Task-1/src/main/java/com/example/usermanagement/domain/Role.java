package com.example.usermanagement.domain;

import java.util.Objects;
import java.util.UUID;

public class Role {
    private final UUID id;
    private String roleName;

    // Constructor for creating a new Role
    public Role(String roleName) {
        this.id = UUID.randomUUID();
        this.roleName = Objects.requireNonNull(roleName, "Role name cannot be null");
        if (roleName.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be blank");
        }
    }

    // Constructor for reconstructing Role from persistence
    public Role(UUID id, String roleName) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.roleName = Objects.requireNonNull(roleName, "Role name cannot be null");
         if (roleName.isBlank()) { // Also validate when reconstructing
            throw new IllegalArgumentException("Role name cannot be blank");
        }
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

     // Setter for roleName if needed (e.g., renaming a role)
    public void setRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or blank");
        }
        this.roleName = roleName;
    }

    // hashCode and equals based on ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Role{" +
               "id=" + id +
               ", roleName='" + roleName + '\'' +
               '}';
    }
} 