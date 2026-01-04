package com.warrantybee.api.dto.internal;

import com.warrantybee.api.enumerations.SecurityPermission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents internal login-related details of a vendor user.
 * Used during authentication and security validation processes.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorLoginUser {

    /** Unique identifier of the vendor user. */
    private Long id;

    /** Encrypted password associated with the vendor user account. */
    private String password;

    /** Indicates whether two-factor authentication is enabled for the user. */
    private Boolean is2FAEnabled;

    /** Indicates the authentication provider used for the user account. */
    private Byte authProvider;

    /** Stores the unique user identifier provided by the external authentication provider. */
    private String authProviderUserId;

    /** The set of security permissions assigned to the user. */
    private List<SecurityPermission> permissions;
}
