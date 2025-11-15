package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** DTO for user sign up request */
@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {

    /** User's first name */
    private String firstname;

    /** User's last name/surname */
    private String lastname;

    /** User's email address */
    private String email;

    /** User's password */
    private String password;

    /** user address line 1 */
    private String addressLine1;

    /** User  address line 2*/
    private String addressLine2;

    /** User state ID */
    private LocalDate dateOfBirth;

    /** User phone number */
    private String phoneNumber;

    /** User gender. The actual type is a {@code Byte} which maps to a value in the {@code Gender} enumeration. */
    private Byte gender;

    /** User region ID */
    private Long regionId;

    /** User country ID */
    private Long countryId;

    /** User city */
    private String city;

    /** User postal code */
    private String postalCode;

    /** User avatar URL */
    private String avatarUrl;

    /** User's preferred culture's identifier */
    private Long cultureId;

    /** Captcha response. */
    private String captchaResponse;
}