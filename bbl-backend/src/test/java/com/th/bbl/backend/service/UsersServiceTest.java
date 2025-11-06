package com.th.bbl.backend.service;

import com.th.bbl.backend.exception.NotFoundException;
import com.th.bbl.backend.exception.ValidationException;
import com.th.bbl.backend.model.UserDTO;
import com.th.bbl.backend.model.UserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        // Create mock users data
        HashSet<UserDTO> mockUsers = new HashSet<>();
        mockUsers.add(new UserDTO(1L, "John Doe", "johndoe", "john@example.com", "123-456-7890", "example.com"));
        mockUsers.add(new UserDTO(2L, "Jane Smith", "janesmith", "jane@example.com", "987-654-3210", "janesmith.com"));

        // Set mock users list in the service
        ReflectionTestUtils.setField(usersService, "users", mockUsers);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Act
        List<UserDTO> result = usersService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).name());
        assertEquals("Jane Smith", result.get(1).name());
    }

    @Test
    void getUserById_ExistingId_ShouldReturnUser() {
        // Act
        Optional<UserDTO> result = usersService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().username());
    }

    @Test
    void getUserById_NonExistingId_ShouldReturnEmpty() {
        // Act
        Optional<UserDTO> result = usersService.getUserById(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void createUser_ValidUser_ShouldAddAndReturnUser() {
        // Arrange
        UserRequestDTO newUser = new UserRequestDTO(
                "Test User",
                "testuser",
                "test@example.com",
                "555-555-5555",
                "test.com"
        );

        // Act
        UserDTO result = usersService.createUser(newUser);

        // Assert
        assertEquals(3L, result.id()); // Expecting ID to be max(existing IDs) + 1
        assertEquals("Test User", result.name());
        assertEquals(3, usersService.getAllUsers().size());
    }

    @Test
    void createUser_MissingEmail_ShouldThrowException() {
        // Arrange
        UserRequestDTO invalidUser = new UserRequestDTO(
                "Test User", "testuser", "sssss", "555-555-5555", "test.com");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () ->
                usersService.createUser(invalidUser));
        assertTrue(exception.getErrors().contains("Invalid email format"));
    }

    @Test
    void createUser_InvalidEmail_ShouldThrowException() {
        // Arrange
        UserRequestDTO invalidUser = new UserRequestDTO(
                "Test User", "testuser", "invalid-email", "555-555-5555", "test.com");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () ->
                usersService.createUser(invalidUser));
        assertTrue(exception.getErrors().contains("Invalid email format"));
    }

    @Test
    void updateUser_ExistingIdAndValidUser_ShouldUpdateAndReturnUser() {
        // Arrange
        UserRequestDTO updatedUser = new UserRequestDTO(
                "Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        usersService.updateUser(1L, updatedUser);

        // Assert
        assertEquals(updatedUser.name(), usersService.getUserById(1L).orElseThrow().name());
    }

    @Test
    void updateUser_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        UserRequestDTO updatedUser = new UserRequestDTO(
                "Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                usersService.updateUser(999L, updatedUser));

        // Assert
        assertEquals("User with ID " + 999L + " not found.", exception.getMessage());
    }

    @Test
    void deleteUser_ExistingId_ShouldReturnTrueAndRemoveUser() {
        // Act
        usersService.deleteUser(1L);

        // Assert
        assertEquals(1, usersService.getAllUsers().size());
        assertFalse(usersService.getUserById(1L).isPresent());
    }

    @Test
    void deleteUser_NonExistingId_ShouldReturnFalse() {
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                usersService.deleteUser(999L)
        );

        // Assert
        assertEquals("User with ID " + 999L + " not found.", exception.getMessage());
        assertEquals(2, usersService.getAllUsers().size());
    }
}
