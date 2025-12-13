package com.warrantybee.api.controllers;

import com.warrantybee.api.dto.request.ForgotPasswordRequest;
import com.warrantybee.api.dto.request.ResetPasswordRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.dto.response.SignUpResponse;
import com.warrantybee.api.dto.response.interfaces.ILoginResponse;
import com.warrantybee.api.services.interfaces.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing user authentication and authorization endpoints,
 * including login and sign-up operations.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService _authService;

    /**
     * Constructs the AuthController and injects the authentication service.
     *
     * @param _authService The service providing core authentication logic.
     */
    public AuthController(IAuthService _authService) {
        this._authService = _authService;
    }

    /**
     * Handles user login by validating credentials and returning an authentication token.
     *
     * @param request The user's login credentials.
     * @return A {@link ResponseEntity} containing a token and user details upon successful login.
     * @throws Exception if an authentication or unexpected error occurs.
     */
    @PostMapping("/login")
    public ResponseEntity<APIResponse<ILoginResponse>> login(@RequestBody LoginRequest request) throws Exception {
        return ResponseEntity.ok(new APIResponse<>(_authService.login(request)));
    }

    /**
     * Handles new user registration by creating a new account.
     *
     * @param request The details required for signing up a new user.
     * @return A {@link ResponseEntity} containing confirmation details upon successful registration.
     * @throws Exception if a registration or unexpected error occurs.
     */
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest request) throws Exception {
        return ResponseEntity.ok(new APIResponse<>(_authService.signUp(request)));
    }

    /**
     * Handles the forgot password request and triggers the OTP process.
     *
     * @param request the forgot password request containing the user's email
     * @return a generic API response indicating success
     * @throws Exception if the process fails
     */
    @PostMapping("/forgotpassword")
    public ResponseEntity<APIResponse<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) throws Exception {
        _authService.forgotPassword(request);
        return ResponseEntity.ok(new APIResponse<>(null, null));
    }

    /**
     * Handles the password reset request using OTP verification.
     *
     * @param request the reset password request containing OTP, email, and new password
     * @return a generic API response indicating success
     * @throws Exception if the process fails
     */
    @PostMapping("/resetpassword")
    public ResponseEntity<APIResponse<?>> resetPassword(@RequestBody ResetPasswordRequest request) throws Exception {
        _authService.resetPassword(request);
        return ResponseEntity.ok(new APIResponse<>(null, null));
    }
}
