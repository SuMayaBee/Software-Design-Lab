package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import lombok.Data;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private Set<String> roles; // Return role names for simplicity

    public static UserResponse fromDomain(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                         .map(Role::getRoleName)
                         .collect(Collectors.toSet()));
        return dto;
    }
} 