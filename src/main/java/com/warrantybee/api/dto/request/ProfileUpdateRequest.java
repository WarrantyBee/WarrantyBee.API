package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the request payload for updating a user's profile information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {

    /**
     * The unique identifier of the user whose profile is being updated.
     */
    private Long userId;

    /**
     * The primary address line of the user's residence.
     */
    private String addressLine1;

    /**
     * An optional secondary address line for additional location details.
     */
    private String addressLine2;

    /**
     * The city where the user resides.
     */
    private String city;

    /**
     * The postal or ZIP code of the user's address.
     */
    private String postalCode;

    /**
     * The identifier of the region/state associated with the user's address.
     */
    private Long regionId;

    /**
     * The identifier of the country associated with the user's address.
     */
    private Long countryId;

    /**
     * The user's phone number without the country/region code.
     */
    private String phoneNumber;

    /**
     * The country or region code associated with the user's phone number.
     */
    private String phoneCode;

    /**
     * The URL of the user's profile picture (avatar).
     */
    private String avatarUrl;
}
