package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for filtering user search results based on ID or email.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserSearchFilter {

    /** User ID to filter by. */
    private Long id;

    /** User email to filter by. */
    private String email;
}
