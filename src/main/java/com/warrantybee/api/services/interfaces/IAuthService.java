package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.request.OtpRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.SignUpResponse;

/**
 * Service interface for handling user authentication and registration operations.
 */
public interface IAuthService {

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param request the login request containing email, password, and captcha
     * @return a {@link LoginResponse} containing user details and authentication token
     */
    LoginResponse login(LoginRequest request);

    /**
     * Registers a new user with the provided sign-up details.
     *
     * @param request the sign-up request containing user registration data
     * @return a {@link SignUpResponse} containing registration confirmation and user info
     */
    SignUpResponse signUp(SignUpRequest request);

    /**
     * Sends an OTP (One-Time Password) to the specified user email for verification.
     *
     * @param request the OTP request containing user ID and email
     */
    void sendOtp(OtpRequest request);
}
