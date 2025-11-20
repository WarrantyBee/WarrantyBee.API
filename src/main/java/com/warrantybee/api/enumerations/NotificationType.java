package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Defines the different types of notifications that can be sent to users,
 * such as OTP messages, welcome emails, or password change alerts.
 */
@Getter
public enum NotificationType implements IEnumeration {

    /** Default or unspecified notification type. */
    NONE(0),

    /** Notification sent when an OTP is requested for multi-factor authentication (login). */
    MFA_LOGIN(1),

    /** Notification sent when an OTP is requested for password recovery. */
    FORGOT_PASSWORD(2),

    /** Notification sent to welcome a new user upon registration. */
    WELCOME(3),

    /** Notification sent when a user successfully changes their password. */
    PASSWORD_CHANGED(4);

    /** The unique integer code associated with this notification type. */
    private final int code;

    /**
     * Constructs a {@code NotificationType} with the specified integer code.
     *
     * @param code the unique code representing this notification type
     */
    NotificationType(int code) {
        this.code = code;
    }
}
