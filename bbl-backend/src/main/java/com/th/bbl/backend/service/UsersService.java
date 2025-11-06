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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsersService {

    private final ConcurrentHashMap<Long, UserDTO> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);


    private final ObjectMapper objectMapper;

    public UsersService() {
        this.objectMapper = new ObjectMapper();
        loadUsersFromJson();
    }

    private void loadUsersFromJson() {
        try {
            InputStream inputStream = new ClassPathResource("users.json").getInputStream();
            List<UserDTO> usersJson = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            log.info("Loaded {} users from JSON file", users.size());
            for (UserDTO user : usersJson) {
                users.put(user.id(), user);
                idCounter.updateAndGet(currentId -> Math.max(currentId, user.id()));
            }
        } catch (IOException e) {
            log.error("Failed to load users from JSON file", e);
        }
    }

    public List<UserDTO> getAllUsers() {
        return users.values().stream()
                .sorted(Comparator.comparing(UserDTO::id))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public void createUser(UserRequestDTO user) {
        // Validate required fields
        List<String> validationErrors = ValidationUtil.validateUser(user);
        if (!validationErrors.isEmpty()) throw new ValidationException(validationErrors);

        long newId = idCounter.incrementAndGet();
        UserDTO newUser = new UserDTO(newId, user.name(), user.username(), user.email(), user.phone(), user.website());
        users.put(newId, newUser);
    }

    public void updateUser(Long id, UserRequestDTO updatedUser) {
        List<String> validationErrors = ValidationUtil.validateUser(updatedUser);
        if (!validationErrors.isEmpty()) throw new ValidationException(validationErrors);

        users.compute(id, (k, existing) -> {
            if (existing == null) throw new NotFoundException("User not found with id " + id);
            return new UserDTO(id, updatedUser.name(), updatedUser.username(), updatedUser.email(), updatedUser.phone(), updatedUser.website());
        });
    }

    public void deleteUser(Long id) {
        UserDTO deleted = users.remove(id);
        if (deleted == null) throw new NotFoundException("User not found with id " + id);
    }

}
