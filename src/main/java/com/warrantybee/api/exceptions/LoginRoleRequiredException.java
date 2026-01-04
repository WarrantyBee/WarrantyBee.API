package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a login role is required but not provided.
 */
public class LoginRoleRequiredException extends APIException {

    /**
     * Constructs a new LoginRoleRequiredException with the default message from {@link Error#LOGIN_ROLE_REQUIRED}.
     */
    public LoginRoleRequiredException() {
        super(Error.LOGIN_ROLE_REQUIRED);
    }

    /**
     * Constructs a new LoginRoleRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public LoginRoleRequiredException(String message) {
        super(Error.LOGIN_ROLE_REQUIRED, message);
    }

    /**
     * Constructs a new LoginRoleRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public LoginRoleRequiredException(String message, Throwable cause) {
        super(Error.LOGIN_ROLE_REQUIRED, message);
        initCause(cause);
    }
}
