package com.example.usermanagement.infrastructure.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Using Lombok @Data for boilerplate code (getters, setters, toString, equals, hashCode)
@Data
public class CreateUserRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
} 