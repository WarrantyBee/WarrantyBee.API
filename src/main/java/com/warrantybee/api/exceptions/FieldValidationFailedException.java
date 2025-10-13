package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.ErrorDefinition;

public class FieldValidationFailedException extends APIException {
    public FieldValidationFailedException(ErrorDefinition definition) {
        super(definition.getError());
    }
}
