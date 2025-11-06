package com.th.bbl.backend.util;

import com.th.bbl.backend.model.UserRequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public static List<String> validateUser(UserRequestDTO user) {
        List<String> errors = new ArrayList<>();
        if (!isValidEmail(user.email())) {
            errors.add("Invalid email format");
        }

        return errors;
    }

    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
