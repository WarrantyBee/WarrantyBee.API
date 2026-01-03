package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when at least one permission is required but not provided.
 */
public class PermissionRequiredException extends APIException {

    /**
     * Constructs a new PermissionRequiredException with the default message from {@link Error#PERMISSION_REQUIRED}.
     */
    public PermissionRequiredException() {
        super(Error.PERMISSION_REQUIRED);
    }

    /**
     * Constructs a new PermissionRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public PermissionRequiredException(String message) {
        super(Error.PERMISSION_REQUIRED, message);
    }

    /**
     * Constructs a new PermissionRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PermissionRequiredException(String message, Throwable cause) {
        super(Error.PERMISSION_REQUIRED, message);
        initCause(cause);
    }
}
