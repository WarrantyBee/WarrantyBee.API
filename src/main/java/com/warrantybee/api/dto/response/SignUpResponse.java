package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for the response sent after a successful user sign-up operation.
 * It contains essential details of the newly created user.
 *
 * The {@code lombok} annotations {@code @Getter} and {@code @Setter} are used to automatically
 * generate the standard getter and setter methods for all fields.
 */
@Getter
@Setter
@AllArgsConstructor
public class SignUpResponse {

    /**
     * The unique identifier (ID) of the newly created user.
     */
    private Long id;
}