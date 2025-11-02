package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.SignUpResponse;

/** Service interface for user authentication */
public interface IAuthService {

    /**
     * Logs in a user.
     *
     * @param request Login request with email, password, and captcha
     * @return LoginResponse containing user info and token
     */
    LoginResponse login(LoginRequest request) throws Exception;

    SignUpResponse signUp(SignUpRequest request) throws Exception;

    void sendOtp(Long userId, String email);
}
