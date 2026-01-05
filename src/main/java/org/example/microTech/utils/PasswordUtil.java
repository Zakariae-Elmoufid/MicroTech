package org.example.microTech.utils;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordUtil {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // strength = 12
    }

    // Verify password
    public static boolean verify(String raw, String hashed) {
        return BCrypt.checkpw(raw, hashed);
    }

}
