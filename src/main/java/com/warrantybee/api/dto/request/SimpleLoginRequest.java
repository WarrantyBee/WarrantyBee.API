package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a simple login request that uses only email and password for authentication.
 */
@Getter
@Setter
@AllArgsConstructor
public class SimpleLoginRequest extends LoginRequest {

}
