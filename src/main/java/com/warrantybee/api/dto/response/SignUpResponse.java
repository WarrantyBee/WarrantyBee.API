package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for the response sent after a successful user sign-up operation.
 *
 * The {@code lombok} annotations {@code @Getter} and {@code @Setter} are used to automatically
 * generate the standard getter and setter methods for all fields.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    /** The unique identifier (ID) of the newly created user. */
    private Long id;
}