package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Represents user profile details returned in API responses.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    /** User's phone number. */
    private String phoneNumber;

    /** Gender of the user. */
    private Gender gender;

    /** Date of birth of the user. */
    private LocalDate dateOfBirth;

    /** Address details of the user. */
    private UserAddressResponse address;

    /** Timezone information associated with the user. */
    private TimeZoneResponse timezone;

    /** Preferred currency of the user. */
    private CurrencyResponse currency;

    /** URL of the user's avatar or profile picture. */
    private String avatarUrl;
}
