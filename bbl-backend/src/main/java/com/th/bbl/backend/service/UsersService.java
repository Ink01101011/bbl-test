package com.th.bbl.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.th.bbl.backend.exception.NotFoundException;
import com.th.bbl.backend.exception.ValidationException;
import com.th.bbl.backend.model.UserDTO;
import com.th.bbl.backend.model.UserRequestDTO;
import com.th.bbl.backend.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class UsersService {

    private HashSet<UserDTO> users;
    private final ObjectMapper objectMapper;

    public UsersService() {
        this.objectMapper = new ObjectMapper();
        loadUsersFromJson();
    }

    private void loadUsersFromJson() {
        try {
            InputStream inputStream = new ClassPathResource("users.json").getInputStream();
            users = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            log.info("Loaded {} users from JSON file", users.size());
        } catch (IOException e) {
            log.error("Failed to load users from JSON file", e);
            users = new HashSet<>();
        }
    }

    public List<UserDTO> getAllUsers() {
        return users.stream().sorted(Comparator.comparing(UserDTO::id)).toList();
    }

    public Optional<UserDTO> getUserById(Long id) {
        return users.stream().filter(user -> user.id().equals(id)).findFirst();
    }

    public UserDTO createUser(UserRequestDTO user) {
        // Validate required fields
        List<String> validationErrors = ValidationUtil.validateUser(user);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        // Assign new ID (max ID + 1)
        Long newId = users.stream()
                .mapToLong(UserDTO::id)
                .max()
                .orElse(0) + 1;
        UserDTO newUser = new UserDTO(newId, user.name(), user.username(), user.email(), user.phone(), user.website());
        users.add(newUser);
        return newUser;
    }

    public void updateUser(Long id, UserRequestDTO updatedUser) {
        // Validate required fields
        List<String> validationErrors = ValidationUtil.validateUser(updatedUser);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        getUserById(id).map(user -> users.remove(user))
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found."));


        UserDTO newUser = new UserDTO(id, updatedUser.name(), updatedUser.username(), updatedUser.email(), updatedUser.phone(), updatedUser.website());
        users.add(newUser);
    }

    public void deleteUser(Long id) {
        getUserById(id)
                .map(user -> users.remove(user))
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found."));
    }

}
