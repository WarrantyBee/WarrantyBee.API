package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.enumerations.VendorContactType;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.repositories.interfaces.IVendorRepository;
import com.warrantybee.api.services.interfaces.IHttpContext;
import com.warrantybee.api.services.interfaces.IVendorService;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing vendor contacts.
 * <p>
 * Applies validation rules and delegates persistence operations using the authenticated vendor context.
 * </p>
 */
@Service
public class VendorService implements IVendorService {
    private final IHttpContext _httpContext;
    private final IVendorRepository _repository;

    /**
     * Creates a new {@code VendorService} with required dependencies.
     *
     * @param httpContext provides access to the current authenticated vendor context
     * @param repository  repository for vendor-related persistence operations
     */
    VendorService(IHttpContext httpContext, IVendorRepository repository) {
        this._httpContext = httpContext;
        this._repository = repository;
    }

    @Override
    public Long addContact(VendorContact contact) {
        _validate(contact, false);
        Long vendorId = _httpContext.getUserId();
        Long contactId = _repository.addContact(vendorId, contact);
        if (contactId == null) {
            throw new VendorContactCreationFailedException();
        }
        return contactId;
    }

    @Override
    public void updateContact(Long contactId, VendorContact contact) {
        _validate(contact, true);
        Long vendorId = _httpContext.getUserId();
        _repository.updateContact(vendorId, contactId, contact);
    }

    @Override
    public void removeContact(Long contactId) {
        Long vendorId = _httpContext.getUserId();
        _repository.removeContact(vendorId, contactId);
    }

    /**
     * Validates the vendor contact payload.
     * @param contact the {@link VendorContact} details to validate
     * @param forUpdate indicates whether the contact is for update operation or not
     */
    private void _validate(VendorContact contact, boolean forUpdate) {
        if (contact == null) {
            throw new RequestBodyEmptyException();
        }
        if (!forUpdate && contact.getType() == null) {
            throw new VendorContactRequiredException();
        }
        if (!forUpdate || contact.getType() != null) {
            if (!Validator.isEnum(contact.getType(), VendorContactType.class)) {
                throw new InvalidVendorContactTypeException();
            }
        }
        if (!forUpdate || contact.getEmail() != null) {
            if (!Validator.isEmail(contact.getEmail())) {
                throw new EmailRequiredException();
            }
        }
        if (!forUpdate || contact.getPhoneNumber() != null) {
            if (!Validator.isPhoneNumber(contact.getPhoneNumber())) {
                throw new InvalidPhoneNumberException();
            }
        }
        if (!forUpdate || contact.getPhoneCode() != null) {
            if (!Validator.isPhoneCode(contact.getPhoneCode())) {
                throw new InvalidPhoneCodeException();
            }
        }
        if (!forUpdate || contact.getCultureId() != null) {
            if (contact.getCultureId() == null) {
                throw new CultureRequiredException();
            }
        }
        if (!forUpdate || contact.getCountryId() != null) {
            if (contact.getCountryId() == null) {
                throw new CountryRequiredException();
            }
        }
        if (!forUpdate || contact.getBusinessHours() != null) {
            if (!Validator.isValidBusinessHours(contact.getBusinessHours())) {
                throw new InvalidBusinessHoursException();
            }
        }
    }
}