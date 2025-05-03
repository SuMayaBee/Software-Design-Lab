package com.example.usermanagement.infrastructure.persistence;

import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users") // Use plural for table name typically
@Getter
@Setter
@NoArgsConstructor // JPA requires a no-arg constructor
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Cascade operations if needed
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleJpaEntity> roles;

    // --- Mappers --- 

    // Convert Domain User to JPA Entity
    public static UserJpaEntity fromDomain(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        // Convert domain Roles to JPA Role Entities
        if (user.getRoles() != null) {
            entity.setRoles(user.getRoles().stream()
                               .map(RoleJpaEntity::fromDomain) // Assumes RoleJpaEntity has a similar static method
                               .collect(Collectors.toSet()));
        }
        return entity;
    }

    // Convert JPA Entity to Domain User
    public User toDomain() {
        // Convert JPA Role Entities back to domain Roles
        Set<Role> domainRoles = null;
        if (this.roles != null) {
            domainRoles = this.roles.stream()
                                  .map(RoleJpaEntity::toDomain) // Assumes RoleJpaEntity has a toDomain method
                                  .collect(Collectors.toSet());
        }
        // Use the constructor that accepts all fields for reconstruction
        return new User(this.id, this.name, this.email, domainRoles);
    }
} 