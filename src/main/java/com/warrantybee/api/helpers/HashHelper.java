package com.warrantybee.api.helpers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Helper class providing secure hashing and verification methods
 * using the Argon2id algorithm for password or sensitive data protection.
 */
public class HashHelper {

    /** Argon2 instance configured with default parameters for hashing and verification. */
    private static final Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id, 32, 64
    );

    /** Number of Argon2 iterations to perform during hashing. */
    private static final int _ITERATIONS = 3;

    /** Memory cost (in kilobytes) used by the Argon2 algorithm. */
    private static final int _MEMORY = 65536;

    /** Degree of parallelism (number of threads) used for hashing. */
    private static final int _PARALLELISM = 1;

    /**
     * Generates a secure Argon2 hash for the specified plain text input.
     *
     * @param text the plain text to hash.
     * @return the generated Argon2 hash as a string.
     * @throws IllegalArgumentException if the input text is {@code null}.
     */
    public static String getHash(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        return argon2.hash(
                _ITERATIONS,
                _MEMORY,
                _PARALLELISM,
                text.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Validates a plain text input against a previously generated Argon2 hash.
     * The comparison is performed in a time-constant manner to prevent timing attacks.
     *
     * @param text the plain text to verify.
     * @param storedHash the Argon2 hash to compare against.
     * @return {@code true} if the text matches the stored hash, {@code false} otherwise.
     */
    public static boolean verify(String text, String storedHash) {
        if (text == null || storedHash == null) {
            return false;
        }

        return argon2.verify(storedHash, text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a random token using SHA256.
     *
     * @return A random token.
     */
    public static String generateToken() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[32];
            secureRandom.nextBytes(randomBytes);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(randomBytes);

            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Converts a byte array into its hexadecimal string representation.
     * Each byte is represented by two hexadecimal characters.
     *
     * @param bytes the byte array to convert
     * @return a hexadecimal string representation of the given bytes
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
