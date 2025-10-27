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
    @NotBlank
    private String firstname;

    /** User's last name/surname */
    @NotBlank
    private String lastname;

    /** User's email address */
    @NotBlank
    @Email
    private String email;

    /** User's password */
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{12,}$")
    private String password;

    /** user address line 1 */
    @NotBlank
    private String addressLine1;

    /** User  address line 2*/
    private String addressLine2;

    /** User date of birth */
    @NotBlank
    private LocalDate dob;

    /** User gender */
    @NotBlank
    private Gender gender;

    /** User state ID */
    @NotBlank
    private Long stateId;

    /** User country ID */
    @NotBlank
    private Long countryId;

    /** User city */
    @NotBlank
    private String city;

    /** User postal code */
    @NotBlank
    private String postalCode;

    /** User avatar URL */
    private String avatarUrl;

    /** Captcha response. */
    @NotBlank
    private String captchaResponse;
}
