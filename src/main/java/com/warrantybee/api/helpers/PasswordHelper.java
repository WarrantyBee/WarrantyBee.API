package com.warrantybee.api.helpers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for secure password hashing and verification using the Argon2 algorithm.
 */
public class PasswordHelper {

    private static final Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id, 32, 64
    );

    private static final int _ITERATIONS = 3;
    private static final int _MEMORY = 65536;
    private static final int _PARALLELISM = 1;

    /**
     * Generates a hex-encoded Argon2 hash for the given plain text password.
     *
     * @param password The plain text password to be hashed.
     * @return The hex string representing the Argon2 hash.
     * @throws IllegalArgumentException if the provided password is null.
     */
    public static String generate(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        return argon2.hash(
                _ITERATIONS,
                _MEMORY,
                _PARALLELISM,
                password.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Verifies that the given plain text password matches the stored hex hash.
     * This method is resistant to timing attacks.
     *
     * @param password The plain text password to verify.
     * @param storedHash The hex string of the previously stored Argon2 hash.
     * @return {@code true} if the password matches the hash, {@code false} otherwise (or if inputs are null).
     */
    public static boolean verify(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }

        return argon2.verify(storedHash, password.getBytes(StandardCharsets.UTF_8));
    }
}
