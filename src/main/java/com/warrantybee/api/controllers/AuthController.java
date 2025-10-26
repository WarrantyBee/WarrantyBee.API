package com.warrantybee.api.controllers;

import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.services.interfaces.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) throws Exception {
        return ResponseEntity.ok(new APIResponse<LoginResponse>(authService.login(request)));
    }
}
