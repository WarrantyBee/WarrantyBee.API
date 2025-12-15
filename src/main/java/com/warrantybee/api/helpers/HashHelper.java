package com.warrantybee.api.helpers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

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

        String argonHash = argon2.hash(_ITERATIONS, _MEMORY, _PARALLELISM, text.getBytes(StandardCharsets.UTF_8));
        return _encodeBase64(argonHash);
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

        String decodedHash = _decodeBase64(storedHash);
        return argon2.verify(decodedHash, text.getBytes(StandardCharsets.UTF_8));
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

            return _bytesToHex(hashedBytes);
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
    private static String _bytesToHex(byte[] bytes) {
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

    /**
     * Encodes the given string into a Base64-encoded string.
     *
     * @param value the plain text to encode
     * @return the Base64-encoded representation of the input
     */
    private static String _encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes the given Base64-encoded string back to its original text.
     *
     * @param base64 the Base64 string to decode
     * @return the decoded plain text
     */
    private static String _decodeBase64(String base64) {
        byte[] decoded = Base64.getDecoder().decode(base64);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
