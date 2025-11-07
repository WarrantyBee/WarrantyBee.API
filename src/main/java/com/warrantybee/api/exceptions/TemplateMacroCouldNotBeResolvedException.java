package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

public class TemplateMacroCouldNotBeResolvedException extends APIException {
    public TemplateMacroCouldNotBeResolvedException() {
        super(Error.TEMPLATE_MACRO_COULD_NOT_BE_RESOLVED);
    }

    public TemplateMacroCouldNotBeResolvedException(String message) {
        super(Error.TEMPLATE_MACRO_COULD_NOT_BE_RESOLVED, message);
    }

    public TemplateMacroCouldNotBeResolvedException(String message, Throwable cause) {
        super(Error.TEMPLATE_MACRO_COULD_NOT_BE_RESOLVED, message);
        initCause(cause);
    }
}
