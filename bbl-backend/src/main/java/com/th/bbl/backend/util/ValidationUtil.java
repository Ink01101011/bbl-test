package com.th.bbl.backend.util;

import com.th.bbl.backend.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public static List<String> validateUser(User user) {
        List<String> errors = new ArrayList<>();

        // Check for required fields
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            errors.add("Name is required");
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            errors.add("Username is required");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!isValidEmail(user.getEmail())) {
            errors.add("Invalid email format");
        }

        return errors;
    }

    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
