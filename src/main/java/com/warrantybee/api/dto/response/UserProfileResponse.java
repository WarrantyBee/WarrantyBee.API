package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) containing comprehensive user profile details.
 * This object is used to return a user's full contact, biographical, and preference information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    /** The user's dialing code (can vary based on the region). */
    private String phoneCode;

    /**
     * The user's primary phone number, typically in E.164 format.
     */
    private String phoneNumber;

    /**
     * The user's gender, represented by the {@link Gender} enumeration.
     */
    private Gender gender;

    /**
     * The user's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * Detailed address information for the user, provided as a {@link UserAddressResponse} DTO.
     */
    private UserAddressResponse address;

    /**
     * The user's preferred timezone details, provided as a {@link TimeZoneResponse} DTO.
     */
    private TimeZoneResponse timezone;

    /**
     * The user's preferred currency settings, provided as a {@link CurrencyResponse} DTO.
     */
    private CurrencyResponse currency;

    /** The user's preferred culture info. */
    private CultureResponse culture;

    /** User's account settings information. */
    private UserSettingsResponse settings;

    /**
     * The public URL pointing to the user's avatar or profile picture image.
     */
    private String avatarUrl;
}
