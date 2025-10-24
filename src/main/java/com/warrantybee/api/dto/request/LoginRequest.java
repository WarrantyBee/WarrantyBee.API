package com.warrantybee.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** DTO for user login request */
@Data
public class LoginRequest {

    /** User email */
    @Email
    @NotBlank
    private String email;

    /** User password */
    @NotBlank
    private String password;
}
