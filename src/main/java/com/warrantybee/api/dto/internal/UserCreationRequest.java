package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Represents user details required for creating a new account.
 * This DTO is used internally within the application services layer.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserCreationRequest {

    /** User's first name. */
    private String firstname;

    /** User's last name. */
    private String lastname;

    /** User's email address, serving as a unique identifier. */
    private String email;

    /** User's login password (should be hashed before persistence). */
    private String password;

    /** User's gender identifier (e.g., 1 for male, 2 for female, etc.). */
    private Byte gender;

    /** User's date of birth. */
    private LocalDate dateOfBirth;

    /** User's phone number. */
    private String phoneNumber;

    /** Primary address line. */
    private String addressLine1;

    /** Secondary address line (optional). */
    private String addressLine2;

    /** Name of the user's city. */
    private String city;

    /** Identifier for the user's region (state/province). */
    private Long regionId;

    /** Identifier for the user's country. */
    private Long countryId;

    /** Postal code for the residence. */
    private String postalCode;

    /** URL of the user's profile avatar image. */
    private String avatarUrl;
}
