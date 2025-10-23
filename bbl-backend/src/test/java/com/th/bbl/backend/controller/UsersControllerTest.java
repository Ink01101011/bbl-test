package com.th.bbl.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.th.bbl.backend.exception.ValidationException;
import com.th.bbl.backend.model.User;
import com.th.bbl.backend.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "John Doe", "johndoe", "john@example.com", "123-456-7890", "example.com");
        user2 = new User(2L, "Jane Smith", "janesmith", "jane@example.com", "987-654-3210", "janesmith.com");
        userList = Arrays.asList(user1, user2);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        // Arrange
        when(usersService.getAllUsers()).thenReturn(userList);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    @Test
    void getUserById_ExistingId_ShouldReturnUser() throws Exception {
        // Arrange
        when(usersService.getUserById(1L)).thenReturn(Optional.of(user1));

        // Act & Assert
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.username", is("johndoe")));
    }

    @Test
    void getUserById_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(usersService.getUserById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ValidUser_ShouldReturnCreatedUser() throws Exception {
        // Arrange
        User newUser = new User(null, "Test User", "testuser", "test@example.com", "555-555-5555", "test.com");
        User createdUser = new User(3L, "Test User", "testuser", "test@example.com", "555-555-5555", "test.com");

        when(usersService.createUser(any(User.class))).thenReturn(createdUser);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Test User")));
    }

    @Test
    void createUser_InvalidUser_ShouldReturnBadRequest() throws Exception {
        // Arrange
        User invalidUser = new User(null, null, "testuser", "test@example.com", "555-555-5555", "test.com");

        when(usersService.createUser(any(User.class))).thenThrow(
            new ValidationException(Arrays.asList("Name is required"))
        );

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.errors", hasItem("Name is required")));
    }

    @Test
    void updateUser_ExistingIdAndValidUser_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        User updatedUser = new User(1L, "Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        when(usersService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        // Act & Assert
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.username", is("updated")));
    }

    @Test
    void updateUser_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        User updatedUser = new User(999L, "Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        when(usersService.updateUser(eq(999L), any(User.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_InvalidUser_ShouldReturnBadRequest() throws Exception {
        // Arrange
        User invalidUser = new User(1L, null, "updated", "updated@example.com", "999-999-9999", "updated.com");

        when(usersService.updateUser(eq(1L), any(User.class))).thenThrow(
            new ValidationException(Arrays.asList("Name is required"))
        );

        // Act & Assert
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.errors", hasItem("Name is required")));
    }

    @Test
    void deleteUser_ExistingId_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(usersService.deleteUser(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(usersService.deleteUser(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}
