package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.dto.request.VendorContactCreationRequest;
import com.warrantybee.api.enumerations.VendorContactType;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.services.interfaces.IHttpContext;
import com.warrantybee.api.services.interfaces.IVendorService;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributeIdentifierException;

@Service
public class VendorService implements IVendorService {
    private final IHttpContext _httpContext;

    VendorService(IHttpContext httpContext) {
        this._httpContext = httpContext;
    }

    @Override
    public void updateContact(VendorContact contact) {
        Long vendorId = _httpContext.getUserId();

    }

    @Override
    public void removeContact(Integer vendorContactId) {
        Long vendorId = _httpContext.getUserId();
    }

    /**
     * Validates the vendor contact request payload.
     * @param request the {@link VendorContact} request to validate
     */
    private void _validate(VendorContact request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        if (request.getType() == null) {
            throw new VendorContactRequiredException();
        }
        if (!Validator.isEnum(request.getType(), VendorContactType.class)) {
            throw new InvalidVendorContactTypeException();
        }
        if (!Validator.isEmail(request.getEmail())) {
            throw new EmailRequiredException();
        }
        if (!Validator.isPhoneNumber(request.getPhoneNumber())) {
            throw new InvalidPhoneNumberException();
        }
        if (!Validator.isPhoneCode(request.getPhoneCode())) {
            throw new InvalidPhoneCodeException();
        }
        if (request.getCultureId() == null) {
            throw new CultureRequiredException();
        }
        if (request.getCountryId() == null) {
            throw new CountryRequiredException();
        }
        if (!Validator.isValidBusinessHours(request.getBusinessHours())) {
            throw new InvalidBusinessHoursException();
        }
    }
}