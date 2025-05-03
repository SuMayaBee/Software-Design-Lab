package com.example.usermanagement.application;

import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.application.interfaces.UserRepository;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
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

@ExtendWith(MockitoExtension.class) // Integrates Mockito with JUnit 5
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks // Creates an instance of UserService and injects the mocks into it
    private UserService userService;

    private User testUser;
    private Role testRole;
    private UUID userId;
    private UUID roleId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        roleId = UUID.randomUUID();
        // Use the reconstructing constructor for test setup
        testUser = new User(userId, "Test User", "test@example.com", null);
        testRole = new Role(roleId, "TEST_ROLE");
    }

    @Test
    void createUser_shouldReturnSavedUser_whenEmailNotExists() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Need to mock the save to return the user with the ID generated in the constructor
        // Capture the argument passed to save
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            // Simulate saving by returning the same user (or a copy with ID)
            // For this test, assume the passed user already has the ID set by its constructor
             return new User(userToSave.getId(), userToSave.getName(), userToSave.getEmail(), userToSave.getRoles());
        });

        // Act
        User createdUser = userService.createUser("New User", "new@example.com");

        // Assert
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId()); // Ensure ID is generated
        assertEquals("New User", createdUser.getName());
        assertEquals("new@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).findByEmail("new@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowException_whenEmailExists() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("Another User", "test@example.com");
        });
        assertEquals("User with email test@example.com already exists.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class)); // Ensure save is not called
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> foundUser = userService.getUserById(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser, foundUser.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_shouldReturnEmpty_whenUserNotExists() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = userService.getUserById(userId);

        // Assert
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void assignRoleToUser_shouldAssignRoleAndReturnUser_whenUserAndRoleExist() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(testRole));
        // Mock the save operation after role assignment
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.assignRoleToUser(userId, roleId);

        // Assert
        assertNotNull(updatedUser);
        assertTrue(updatedUser.getRoles().contains(testRole));
        assertEquals(1, updatedUser.getRoles().size());
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findById(roleId);
        verify(userRepository, times(1)).save(testUser); // Verify save was called with the updated user
    }

    @Test
    void assignRoleToUser_shouldThrowUserNotFound_whenUserNotExist() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // Role repository findById doesn't need to be mocked here as it won't be reached

        // Act & Assert
        UserService.UserNotFoundException exception = assertThrows(UserService.UserNotFoundException.class, () -> {
            userService.assignRoleToUser(userId, roleId);
        });
        assertEquals("User not found with ID: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, never()).findById(any(UUID.class));
        verify(userRepository, never()).save(any(User.class));
    }

     @Test
    void assignRoleToUser_shouldThrowRoleNotFound_whenRoleNotExist() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        UserService.RoleNotFoundException exception = assertThrows(UserService.RoleNotFoundException.class, () -> {
            userService.assignRoleToUser(userId, roleId);
        });
        assertEquals("Role not found with ID: " + roleId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findById(roleId);
        verify(userRepository, never()).save(any(User.class));
    }

} 