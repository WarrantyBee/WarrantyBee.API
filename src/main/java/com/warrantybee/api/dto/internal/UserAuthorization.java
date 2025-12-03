package com.warrantybee.api.dto.internal;

import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.SecurityRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a user's role and permissions.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserAuthorization {

    /**
     * Initializes with default role and empty permission list.
     */
    public UserAuthorization() {
        role = SecurityRole.NONE;
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
    }

    /** User's assigned role. */
    private SecurityRole role;

    /** Permissions granted to the user. */
    private List<SecurityPermission> permissions;

    /**
     * Adds a permission to the user.
     *
     * @param permission permission to add
     */
    public void addPermission(SecurityPermission permission) {
        permissions.add(permission);
    }
}
