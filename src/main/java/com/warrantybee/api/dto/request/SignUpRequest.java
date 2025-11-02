package com.warrantybee.api.dto.request;

import com.warrantybee.api.enumerations.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** DTO for user sign up request */
@Getter
@Setter
public class SignUpRequest {

    /** User's first name */
    private String firstname;

    /** User's last name/surname */
    private String lastname;

    /** User's email address */
    private String email;

    /** User's password */
<<<<<<< HEAD
    @NotBlank
=======
>>>>>>> c9d7bb07b095b0d806ba915930111a3f05f419c9
    private String password;

    /** user address line 1 */
    private String addressLine1;

    /** User  address line 2*/
    private String addressLine2;

    /** User date of birth */
<<<<<<< HEAD
    private LocalDate dob;

    /** User gender */
    private Gender gender;

    /** User state ID */
=======
    private LocalDate dateOfBirth;

    /** User phone number */
    private String phoneNumber;

    /** User gender */
    private Byte gender;

    /** User region ID */
>>>>>>> c9d7bb07b095b0d806ba915930111a3f05f419c9
    private Long regionId;

    /** User country ID */
    private Long countryId;

    /** User city */
    private String city;

    /** User postal code */
    private String postalCode;

    /** User avatar URL */
    private String avatarUrl;

    /** Captcha response. */
    private String captchaResponse;
}
