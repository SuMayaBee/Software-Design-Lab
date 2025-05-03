package com.example.usermanagement.infrastructure.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleRequest {
    @NotBlank(message = "Role name cannot be blank")
    private String roleName;
} 