package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.Gender;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/** DTO representing user profile details in responses */
@Getter
@Setter
public class UserProfileResponse {

    /** Phone number */
    private String phoneNumber;

    /** Gender of the user */
    private Gender gender;

    /** Date of birth */
    private LocalDate dateOfBirth;

    /** Address line 1 */
    private String addressLine1;

    /** Address line 2 */
    private String addressLine2;

    /** State name */
    private String state;

    /** Country name */
    private String country;

    /** City */
    private String city;

    /** Postal code */
    private String postalCode;

    /** Avatar URL */
    private String avatarUrl;
}
