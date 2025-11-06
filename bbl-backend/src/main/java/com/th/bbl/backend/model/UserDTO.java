package com.th.bbl.backend.model;

import lombok.NonNull;

public record UserDTO(Long id, @NonNull String name,
                      @NonNull String username,
                      @NonNull String email,
                      String phone,
                      String website) {
}
