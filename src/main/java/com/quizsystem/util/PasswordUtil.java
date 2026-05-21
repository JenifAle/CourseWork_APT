package com.quizsystem.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords using Bcrypt.
 *
 * Reference: Lecture 5 (Security with Encryption and Hashing).
 *  - Bcrypt automatically generates a salt
 *  - The salt is included in the final 60-character hash
 *  - The default cost factor of 10 is recommended
 */
public class PasswordUtil {

    private static final int COST = 10;   // bcrypt rounds (Lecture 5: 10–12 typical)

    /**
     * Hashes a plain-text password using Bcrypt.
     *
     * @param plainPassword raw password from the user
     * @return 60-character bcrypt hash to be stored in the database
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
    }

    /**
     * Verifies a plain-text password against a stored bcrypt hash.
     *
     * @param plainPassword the password the user typed
     * @param hashedPassword the bcrypt hash stored in the database
     * @return true if the password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException ex) {
            // Thrown if the stored value is not a valid bcrypt hash
            return false;
        }
    }
}
