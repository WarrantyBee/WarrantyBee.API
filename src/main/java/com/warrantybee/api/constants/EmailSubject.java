package com.warrantybee.api.constants;

/**
 * Defines constant subjects used in OTP-related emails.
 */
public class EmailSubject {

    /** Subject for welcome email. */
    public static final String NEW_ACCOUNT = "Welcome to WarrantyBee!";

    /** Subject for login OTP emails. */
    public static final String LOGIN = "Your OTP for login";

    /** Subject for forgot password OTP emails. */
    public static final String FORGOT_PASSWORD = "Your OTP for resetting password";

    /** Subject for password changed notification emails. */
    public static final String PASSWORD_CHANGED = "Your password has been changed";
}
