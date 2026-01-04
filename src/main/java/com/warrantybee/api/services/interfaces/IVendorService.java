package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.dto.request.VendorLoginCreationRequest;

/** Defines vendor-related operations within the system. */
public interface IVendorService {

    /**
     * Adds a new vendor contact.
     *
     * @param contact the vendor contact details to persist
     * @return the unique identifier of the created contact, or {@code null} if creation fails
     */
    Long addContact(VendorContact contact);

    /**
     * Creates a new vendor contact or updates an existing one.
     * @param contactId the unique identifier of the contact to update.
     * @param contact   the vendor contact details
     */
    void updateContact(Long contactId, VendorContact contact);

    /**
     * Removes a vendor contact identified by its unique ID.
     * @param contactId the unique identifier of the vendor contact to remove
     */
    void removeContact(Long contactId);

    /**
     * Creates login credentials for a vendor-associated user.
     * @param vendorId the vendor identifier
     * @param request request containing vendor and user identifier
     * @returns newly created vendor login identifier
     */
     Long createVendorLogin(Long vendorId, VendorLoginCreationRequest request);
}
