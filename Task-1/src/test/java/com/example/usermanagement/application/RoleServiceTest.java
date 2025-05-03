package com.example.usermanagement.application;

import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role testRole;
    private UUID roleId;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();
        testRole = new Role(roleId, "ADMIN"); // Use reconstructing constructor
    }

    @Test
    void createRole_shouldReturnSavedRole_whenRoleNameNotExists() {
        // Arrange
        String roleName = "NEW_ROLE";
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());
        // Mock save to return the role with ID generated in constructor
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            Role roleToSave = invocation.getArgument(0);
            return new Role(roleToSave.getId(), roleToSave.getRoleName());
        });

        // Act
        Role createdRole = roleService.createRole(roleName);

        // Assert
        assertNotNull(createdRole);
        assertNotNull(createdRole.getId());
        assertEquals(roleName, createdRole.getRoleName());
        verify(roleRepository, times(1)).findByRoleName(roleName);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void createRole_shouldThrowException_whenRoleNameExists() {
        // Arrange
        String existingRoleName = "ADMIN";
        when(roleRepository.findByRoleName(existingRoleName)).thenReturn(Optional.of(testRole));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roleService.createRole(existingRoleName);
        });
        assertEquals("Role with name " + existingRoleName + " already exists.", exception.getMessage());
        verify(roleRepository, times(1)).findByRoleName(existingRoleName);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void getRoleById_shouldReturnRole_whenRoleExists() {
        // Arrange
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(testRole));

        // Act
        Optional<Role> foundRole = roleService.getRoleById(roleId);

        // Assert
        assertTrue(foundRole.isPresent());
        assertEquals(testRole, foundRole.get());
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    void getRoleById_shouldReturnEmpty_whenRoleNotExists() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(roleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<Role> foundRole = roleService.getRoleById(nonExistentId);

        // Assert
        assertFalse(foundRole.isPresent());
        verify(roleRepository, times(1)).findById(nonExistentId);
    }
} 