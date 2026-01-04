package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.dto.request.VendorLoginCreationRequest;
import com.warrantybee.api.enumerations.AuthProvider;
import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.VendorContactType;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.HashHelper;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.repositories.interfaces.IVendorRepository;
import com.warrantybee.api.services.interfaces.IHttpContext;
import com.warrantybee.api.services.interfaces.IVendorService;
import org.springframework.stereotype.Service;

import java.security.Permission;

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

    @Override
    public Long createVendorLogin(Long vendorId, VendorLoginCreationRequest request) {
        _validate(request);
        final String hashedPassword = HashHelper.getHash(request.getPassword());
        request.setPassword(hashedPassword);
        Long id = _repository.createVendorLogin(vendorId, request);
        if (id == null) {
            throw new VendorLoginExistsException();
        }
        return id;
    }

    /**
     * Validates the vendor login creation request.
     * @param request request to validate
     */
    private void _validate(VendorLoginCreationRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }

        AuthProvider authProvider = AuthProvider.getValue(request.getAuthProvider());
        if (request.getUserId() == null) {
            throw new UserIdentifierRequiredException();
        }
        if (!Validator.isEnum(request.getAuthProvider(), AuthProvider.class)) {
            throw new AuthProviderNotSupportedException();
        }
        if (authProvider == AuthProvider.INTERNAL && Validator.isBlank(request.getPassword())) {
            throw new PasswordRequiredException();
        }
        if (!Validator.isStrongPassword(request.getPassword())) {
            throw new StrongPasswordRequiredException();
        }
        if (!Validator.any(request.getPermissions())) {
            throw new PermissionRequiredException();
        }
        boolean hasInvalidPermission = request.getPermissions().stream().anyMatch(r -> !Validator.isEnum(r, SecurityPermission.class));
        if (hasInvalidPermission) {
            throw new PermissionRequiredException();
        }
        if (authProvider != AuthProvider.INTERNAL && Validator.isBlank(request.getAuthProviderUserId())) {
            throw new AuthProviderUserIdentifierRequiredException();
        }
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