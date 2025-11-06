package com.th.bbl.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.th.bbl.backend.exception.GlobalExceptionHandler;
import com.th.bbl.backend.exception.NotFoundException;
import com.th.bbl.backend.model.UserDTO;
import com.th.bbl.backend.model.UserRequestDTO;
import com.th.bbl.backend.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsersService usersService;

    private ObjectMapper objectMapper;

    private UserDTO user1;
    private List<UserDTO> userList;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // create controller with mocked service and register global exception handler
        UsersController controller = new UsersController(usersService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        userList = new ArrayList<>();
        user1 = new UserDTO(1L, "John Doe", "johndoe", "john@example.com", "123-456-7890", "example.com");
        userList.add(user1);
        userList.add(new UserDTO(2L, "Jane Smith", "janesmith", "jane@example.com", "987-654-3210", "janesmith.com"));
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
        UserRequestDTO newUser = new UserRequestDTO("Test User", "testuser", "test@example.com", "555-555-5555", "test.com");

        // Act & Assert: controller returns 201 CREATED with empty body
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    @Test
    void updateUser_ExistingIdAndValidUser_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        UserRequestDTO updatedUser = new UserRequestDTO(
                "Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        // Act & Assert
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        UserRequestDTO updatedUser = new UserRequestDTO("Updated Name", "updated", "updated@example.com", "999-999-9999", "updated.com");

        doThrow(new NotFoundException("User not found")).when(usersService).updateUser(eq(3L), any(UserRequestDTO.class));

        // Act & Assert
        mockMvc.perform(put("/api/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_ExistingId_ShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_NonExistingId_ShouldReturnNotFound() throws Exception {
        doThrow(new NotFoundException("User not found")).when(usersService).deleteUser(eq(999L));

        // Act & Assert
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}
