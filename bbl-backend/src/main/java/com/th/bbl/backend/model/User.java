package com.th.bbl.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
}
