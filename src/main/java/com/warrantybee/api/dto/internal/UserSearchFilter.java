package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for filtering user search results based on ID or email.
 * This is typically used internally by services to query the data layer.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserSearchFilter {

    /** The unique identifier (ID) of the user to filter by. Can be null. */
    private Long id;

    /** The email address of the user to filter by. Can be null. */
    private String email;
}
