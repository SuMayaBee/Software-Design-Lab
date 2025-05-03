package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.RoleService;
import com.example.usermanagement.domain.Role;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        Role newRole = roleService.createRole(request.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", newRole.getId()));
    }

    // Optional: Add GET endpoint for Role details if needed
    // @GetMapping("/{id}")
    // public ResponseEntity<Role> getRoleById(@PathVariable UUID id) {
    //     return roleService.getRoleById(id)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.notFound().build());
    // }
} 