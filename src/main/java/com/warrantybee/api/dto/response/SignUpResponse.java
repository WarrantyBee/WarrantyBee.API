package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for the response returned after a successful user sign-up.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    /**
     * The unique identifier (ID) of the newly created user.
     */
    private Long id;
}
