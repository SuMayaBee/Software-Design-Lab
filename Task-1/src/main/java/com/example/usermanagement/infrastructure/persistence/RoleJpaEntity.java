package com.example.usermanagement.infrastructure.persistence;

import com.example.usermanagement.domain.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor // JPA requires a no-arg constructor
public class RoleJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String roleName;

    // Note: We don't typically store the inverse relationship (users) here
    // unless strictly necessary, to avoid potential complexities.
    // If needed, it would be a @ManyToMany mappedBy="roles".

    // --- Mappers --- 

    // Convert Domain Role to JPA Entity
    public static RoleJpaEntity fromDomain(Role role) {
        RoleJpaEntity entity = new RoleJpaEntity();
        entity.setId(role.getId());
        entity.setRoleName(role.getRoleName());
        return entity;
    }

    // Convert JPA Entity to Domain Role
    public Role toDomain() {
        // Use the constructor that accepts all fields for reconstruction
        return new Role(this.id, this.roleName);
    }
} 