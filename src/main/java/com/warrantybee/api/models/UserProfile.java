package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/** Represents a user's profile information */
@Entity
@Table(name = "tblUserProfiles")
@Getter
@Setter
public class UserProfile extends BaseEntity<UserProfile> {

    /** Phone number */
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    /** Gender (e.g., 0=Male, 1=Female) */
    @Column(name = "gender", nullable = false)
    private Byte gender;

    /** Date of birth */
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /** Address line 1 */
    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;

    /** Address line 2 */
    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    /** State ID */
    @Column(name = "state_id", nullable = false)
    private Long regionId;

    /** Country ID */
    @Column(name = "country_id", nullable = false)
    private Long countryId;

    /** City */
    @Column(name = "city", nullable = false, length = 255)
    private String city;

    /** Postal code */
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    /** Avatar URL */
    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;

    /** Associated user ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** Associated state entity */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", updatable = false, insertable = false)
    private Region region;

    /** Associated country entity */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", updatable = false, insertable = false)
    private Country country;

    /** Associated user entity */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false, unique = true)
    private User user;
}
