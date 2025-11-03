package com.warrantybee.api.controllers;

import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.request.OtpRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.SignUpResponse;
import com.warrantybee.api.services.interfaces.IAuthService;
import jakarta.validation.Valid;
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
    private final IAuthService authService;

    /**
     * Constructs the AuthController and injects the authentication service.
     *
     * @param authService The service providing core authentication logic.
     */
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles user login by validating credentials and returning an authentication token.
     *
     * @param request The user's login credentials.
     * @return A {@link ResponseEntity} containing a token and user details upon successful login.
     * @throws Exception if an authentication or unexpected error occurs.
     */
    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@RequestBody LoginRequest request) throws Exception {
        return ResponseEntity.ok(new APIResponse<LoginResponse>(authService.login(request)));
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
        return ResponseEntity.ok(new APIResponse<SignUpResponse>(authService.signUp(request)));
    }

    @PostMapping("/sendotp")
    public ResponseEntity<APIResponse<?>> sendOtp(@RequestBody OtpRequest request) throws Exception {
        return ResponseEntity.ok(new APIResponse<?>(authService/))
    }
}
