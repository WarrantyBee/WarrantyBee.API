package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.dto.internal.VendorLoginUser;
import com.warrantybee.api.dto.request.VendorLoginCreationRequest;

/** Provides data access operations for managing vendors and their details. */
public interface IVendorRepository {

    /**
     * Adds a new contact for the specified vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @param contact  the vendor contact details to add
     * @returns the newly inserted contact's identifier
     */
    Long addContact(Long vendorId, VendorContact contact);

    /**
     * Updates or persists a vendor contact for the specified vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @param contactId the unique identifier of the contact to be deleted
     * @param contact the vendor contact details to be updated
     */
    void updateContact(Long vendorId, Long contactId, VendorContact contact);

    /**
     * Removes a vendor contact associated with the specified vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @param contactId the unique identifier of the contact to be deleted
     */
    void removeContact(Long vendorId, Long contactId);

    /**
     * Creates a login for a vendor user.
     * @param vendorId the vendor identifier
     * @param request vendor login creation request
     * @returns newly created vendor login identifier
     */
    Long createVendorLogin(Long vendorId, VendorLoginCreationRequest request);

    /**
     * Retrieves the login details of a vendor user by user ID.
     *
     * @param userId the unique identifier of the user
     * @return the vendor login user details
     */
    VendorLoginUser getLoginUser(Long userId);
}
