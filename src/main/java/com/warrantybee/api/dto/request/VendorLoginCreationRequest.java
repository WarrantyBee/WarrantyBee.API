package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Request DTO used to create login credentials for a vendor user. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorLoginCreationRequest {

    /** Identifier of the user associated with the vendor. */
    private Long userId;

    /** Raw password for login creation. */
    private String password;

    /** Flag indicating whether two-factor authentication is enabled. */
    private Boolean is2FAEnabled;

    /** permissions associated with the vendor login. */
    private List<Integer> permissions;

    /** Identifier of the authentication provider used for login. */
    private Integer authProvider;

    /** Unique user identifier returned by the authentication provider. */
    private String authProviderUserId;
}
