package com.warrantybee.api.dto.internal;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Represents user details required for creating a new account.
 */
@Getter
@Setter
public class UserCreationRequest {

    /** User's first name. */
    private String firstname;

    /** User's last name. */
    private String lastname;

    /** User's email address. */
    private String email;

    /** User's login password. */
    private String password;

    /** User's phone number. */
    private String phoneNumber;

    /** User's gender (e.g., 1 for male, 2 for female, etc.). */
    private Byte gender;

    /** User's date of birth. */
    private LocalDate dob;

    /** Primary address line. */
    private String addressLine1;

    /** Secondary address line (optional). */
    private String addressLine2;

    /** Name of the user's city. */
    private String city;

    /** Identifier for the user's region. */
    private Long regionId;

    /** Identifier for the user's country. */
    private Long countryId;

    /** Identifier for the user's postal code. */
    private String postalCode;

    /** URL of the user's profile avatar image. */
    private String avatarUrl;
}
