package com.warrantybee.api.services.implementations;

import com.warrantybee.api.models.User;
import com.warrantybee.api.services.interfaces.IOtpService;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

/**
 * Implementation of {@link IOtpService} that generates
 * a cryptographically secure, 6-digit one-time password (OTP).
 */
@Service
public class OtpService implements IOtpService {

    private final SecureRandom _secureRandom;

    /**
     * Constructs a new OtpService and initializes a {@link SecureRandom} instance.
     */
    public OtpService() {
        _secureRandom = new SecureRandom();
    }

    /**
     * Generates a new, cryptographically secure, 6-digit OTP.
     * The number is padded with leading zeros to ensure it is always 6 digits.
     *
     * @return A {@code String} representing the generated 6-digit OTP.
     */
    @Override
    public String generate() {
        int otp = _secureRandom.nextInt(1_000_000);
        return String.format("%06d", otp);
    }
}