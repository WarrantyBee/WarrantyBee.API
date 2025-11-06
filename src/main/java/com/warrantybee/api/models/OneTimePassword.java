package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a one-time password (OTP) entity used for authentication or verification purposes.
 * Stores the OTP value, the recipient information, and an optional recipient ID for mapping.
 */
@Entity
@Table(name = "tblOtp")
@Getter
@Setter
public class OneTimePassword extends BaseEntity<OneTimePassword> {

    /** The OTP value sent to the recipient. */
    @Column(name = "value", nullable = false)
    private String value;

    /** The recipient (e.g., email) whom the OTP was sent. */
    @Column(name = "recipient", nullable = false)
    private String recipient;

    /** The unique identifier of the recipient, if available. */
    @Column(name = "recipient_id")
    private Long recipientId;

    /** Associated user entity */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", updatable = false, insertable = false)
    private User user;
}
