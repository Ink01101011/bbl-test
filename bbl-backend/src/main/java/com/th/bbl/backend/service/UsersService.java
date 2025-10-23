package com.th.bbl.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.th.bbl.backend.exception.ValidationException;
import com.th.bbl.backend.model.User;
import com.th.bbl.backend.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsersService {

    private List<User> users;
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
            users = new ArrayList<>();
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User createUser(User user) {
        // Validate required fields
        List<String> validationErrors = ValidationUtil.validateUser(user);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        // Assign new ID (max ID + 1)
        Long newId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;

        user.setId(newId);
        users.add(user);
        return user;
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        // Validate required fields
        List<String> validationErrors = ValidationUtil.validateUser(updatedUser);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        return getUserById(id)
                .map(existingUser -> {
                    // Update fields
                    existingUser.setName(updatedUser.getName());
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPhone(updatedUser.getPhone());
                    existingUser.setWebsite(updatedUser.getWebsite());
                    return existingUser;
                });
    }

    public boolean deleteUser(Long id) {
        return getUserById(id)
                .map(user -> users.remove(user))
                .orElse(false);
    }
}
