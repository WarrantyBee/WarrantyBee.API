package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.request.VendorContactCreationRequest;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.services.interfaces.IVendorService;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributeIdentifierException;

@Service
public class VendorService implements IVendorService {

    private void _validate(VendorContactCreationRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        if (request.getType() == null) {
            throw new UserIdentifierRequiredException();
        }
        if (request.getEmail() != null && Validator.isBlank(request.getEmail())) {
            throw new EmailRequiredException("");
        }
        if (request.getPhoneNumber() != null && Validator.isPhoneNumber(request.getPhoneNumber())) {
            throw new InvalidPhoneNumberException("");
        }
        if (request.getPhoneCode() != null && !Validator.isPhoneCode(request.getPhoneCode())) {
            throw new InvalidPhoneCodeException("");
        }
        if (request.getCultureId() != null && !Validator.isCultureId(request.getCultureId())) {
            throw new InvalidIdentifierException();
        }
        if (request.getCountryId() != null && !Validator.isCountryId(request.getCountryId())) {
            throw new InvalidIdentifierException();
        }
        if (request.getBusinessHours() != null && !Validator.isValidBusinessHours(request.getBusinessHours())) {
            throw new InvalidBusinessHoursException();
        }
    }
