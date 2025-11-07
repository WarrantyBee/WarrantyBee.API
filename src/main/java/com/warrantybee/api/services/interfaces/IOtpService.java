package com.warrantybee.api.services.interfaces;

/**
 * Service interface for one-time password (OTP) operations.
 */
public interface IOtpService {
    /**
     * Generates a new, time-sensitive one-time password (OTP).
     *
     * @return A {@code String} representing the generated OTP.
     */
    String generate();
}