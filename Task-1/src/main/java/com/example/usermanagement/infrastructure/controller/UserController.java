package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.UserService;
import com.example.usermanagement.domain.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> createUser(@Valid @RequestBody CreateUserRequest request) {
        User newUser = userService.createUser(request.getName(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", newUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(UserResponse::fromDomain) // Map domain User to UserResponse DTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/assign-role/{roleId}")
    public ResponseEntity<Map<String, String>> assignRoleToUser(@PathVariable UUID userId, @PathVariable UUID roleId) {
         // Exception handling for not found is done in the service/advice
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(Map.of("message", "Role assigned successfully"));

    }

    // We'll add a GlobalExceptionHandler later for better error handling
} 