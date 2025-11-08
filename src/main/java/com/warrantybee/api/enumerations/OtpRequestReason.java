package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Defines reasons for which an OTP can be requested.
 */
@Getter
public enum OtpRequestReason implements IEnumeration {
    /** No specific reason. */
    None(0),

    /** OTP requested for user login. */
    Login(1),

    /** OTP requested for password recovery. */
    ForgotPassword(2);

    /**
     * The unique, persistent integer code associated with this reason.
     */
    private final int code;

    /**
     * Constructs a {@code OtpRequestReason} enum constant with the specified code.
     *
     * @param code The unique integer code.
     */
    OtpRequestReason(int code) {
        this.code = code;
    }
}
