package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.request.ForgotPasswordRequest;
import com.warrantybee.api.dto.request.ResetPasswordRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.request.interfaces.ILoginRequest;
import com.warrantybee.api.dto.response.SignUpResponse;
import com.warrantybee.api.dto.response.interfaces.ILoginResponse;

/**
 * Service interface for handling user authentication and registration operations.
 */
public interface IAuthService {

    /**
     * Authenticates a user based on the provided login request and returns a corresponding login response.
     *
     * @param request the login request containing user credentials and related details
     * @return an {@link ILoginResponse} representing the outcome of the login process
     */
    ILoginResponse login(ILoginRequest request);

    /**
     * Registers a new user with the provided sign-up details.
     *
     * @param request the sign-up request containing user registration data
     * @return a {@link SignUpResponse} containing registration confirmation and user info
     */
    SignUpResponse signUp(SignUpRequest request);

    /**
     * Initiates the forgot password process for the given user.
     *
     * @param request contains the user's email address
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * Resets the user's password after OTP verification.
     *
     * @param request contains the OTP, email, and new password
     */
    void resetPassword(ResetPasswordRequest request);
}
