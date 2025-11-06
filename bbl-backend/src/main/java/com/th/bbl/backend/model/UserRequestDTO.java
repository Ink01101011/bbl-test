package com.th.bbl.backend.model;

import lombok.NonNull;

public record UserRequestDTO(
        @NonNull String name,
        @NonNull String username,
        @NonNull String email,
        String phone,
        String website) {
}
