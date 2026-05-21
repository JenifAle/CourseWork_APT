package com.quizsystem.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 * Used by servlets to validate user input from forms before saving to the DB.
 */
public class ValidationUtil {

    // Simple email regex (sufficient for college coursework)
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Phone number must be exactly 10 digits (Nepal mobile format)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    /**
     * Returns true if the value is null OR after trimming is empty.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates email format using a simple regex.
     */
    public static boolean isValidEmail(String email) {
        return !isNullOrEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates phone number — must be exactly 10 digits.
     */
    public static boolean isValidPhone(String phone) {
        return !isNullOrEmpty(phone) && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates password — must be at least 6 characters and contain
     * at least one letter and one digit.
     */
    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password) || password.length() < 6) return false;
        boolean hasLetter = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    /**
     * Validates full name — letters and spaces only, length 3–50.
     */
    public static boolean isValidName(String name) {
        if (isNullOrEmpty(name)) return false;
        String n = name.trim();
        return n.length() >= 3 && n.length() <= 50 && n.matches("^[A-Za-z ]+$");
    }
}
