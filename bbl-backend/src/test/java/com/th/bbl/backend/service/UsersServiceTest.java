package com.th.bbl.backend.service;

import com.th.bbl.backend.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    private List<User> mockUsers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock users data
        mockUsers = new ArrayList<>();
        mockUsers.add(new User(1L, "John Doe", "johndoe", "john@example.com", "123-456-7890", "example.com"));
        mockUsers.add(new User(2L, "Jane Smith", "janesmith", "jane@example.com", "987-654-3210", "janesmith.com"));

        // Set mock users list in the service
        ReflectionTestUtils.setField(usersService, "users", mockUsers);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Act
        List<User> result = usersService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
    }

    @Test
    void getUserById_ExistingId_ShouldReturnUser() {
        // Act
        Optional<User> result = usersService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().getUsername());
    }

    @Test
    void getUserById_NonExistingId_ShouldReturnEmpty() {
        // Act
        Optional<User> result = usersService.getUserById(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void createUser_ValidUser_ShouldAddAndReturnUser() {
        // Arrange
        User newUser = new User(null, "Test User", "testuser", "test@example.com", "555-555-5555", "test.com");

        // Act
        User result = usersService.createUser(newUser);

        // Assert
        assertEquals(3L, result.getId()); // Expecting ID to be max(existing IDs) + 1
        assertEquals("Test User", result.getName());
        assertEquals(3, usersService.getAllUsers().size());
    }

    @Test
    void createUser_MissingName_ShouldThrowException() {
        // Arrange
        User invalidUser = new User(null, null, "testuser", "test@example.com", "555-555-5555", "test.com");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usersService.createUser(invalidUser);
        });
        assertTrue(exception.getErrors().contains("Name is required"));
    }

    @Test
    void createUser_MissingUsername_ShouldThrowException() {
        // Arrange
        User invalidUser = new User(null, "Test User", null, "test@example.com", "555-555-5555", "test.com");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usersService.createUser(invalidUser);
        });
        assertTrue(exception.getErrors().contains("Username is required"));
    }

    @Test
    void createUser_MissingEmail_ShouldThrowException() {
        // Arrange
        User invalidUser = new User(null, "Test User", "testuser", null, "555-555-5555", "test.com");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usersService.createUser(invalidUser);
        });
        assertTrue(exception.getErrors().contains("Email is required"));
    }

    @Test
    void createUser_InvalidEmail_ShouldThrowException() {
        // Arrange
        User invalidUser = new User(null, "Test User", "testuser", "invalid-email", "555-555-5555", "test.com");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usersService.createUser(invalidUser);
        });
        assertTrue(exception.getErrors().contains("Invalid email format"));
    }

    @Test
    void updateUser_ExistingIdAndValidUser_ShouldUpdateAndReturnUser() {
        // Arrange
        User updatedUser = new User(1L, "Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        // Act
        Optional<User> result = usersService.updateUser(1L, updatedUser);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals("updated", result.get().getUsername());
        assertEquals("updated@example.com", result.get().getEmail());
    }

    @Test
    void updateUser_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        User updatedUser = new User(999L, "Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        // Act
        Optional<User> result = usersService.updateUser(999L, updatedUser);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void updateUser_InvalidUser_ShouldThrowException() {
        // Arrange
        User invalidUser = new User(1L, null, "updated", "updated@example.com", "999-999-9999", "updated.com");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usersService.updateUser(1L, invalidUser);
        });
        assertTrue(exception.getErrors().contains("Name is required"));
    }

    @Test
    void deleteUser_ExistingId_ShouldReturnTrueAndRemoveUser() {
        // Act
        boolean result = usersService.deleteUser(1L);

        // Assert
        assertTrue(result);
        assertEquals(1, usersService.getAllUsers().size());
        assertFalse(usersService.getUserById(1L).isPresent());
    }

    @Test
    void deleteUser_NonExistingId_ShouldReturnFalse() {
        // Act
        boolean result = usersService.deleteUser(999L);

        // Assert
        assertFalse(result);
        assertEquals(2, usersService.getAllUsers().size());
    }
}
