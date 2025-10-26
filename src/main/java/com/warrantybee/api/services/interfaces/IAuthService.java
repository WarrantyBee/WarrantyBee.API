package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.response.LoginResponse;

/** Service interface for user authentication */
public interface IAuthService {

    /**
     * Logs in a user.
     *
     * @param request Login request with email, password, and captcha
     * @return LoginResponse containing user info and token
     */
    LoginResponse login(LoginRequest request) throws Exception;
}
