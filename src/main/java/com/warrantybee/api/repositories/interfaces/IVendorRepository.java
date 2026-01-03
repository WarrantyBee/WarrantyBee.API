package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.internal.VendorContact;

/**
 * Provides data access operations for managing vendors and their details.
 */
public interface IVendorRepository {

    /**
     * Updates or persists a vendor contact for the specified vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @param contactId the unique identifier of the contact to be deleted
     * @param contact the vendor contact details to be updated
     */
    void updateContact(Long vendorId, Long contactId, VendorContact contact);

    /**
     * Deletes a vendor contact associated with the specified vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @param contactId the unique identifier of the contact to be deleted
     */
    void deleteContact(Long vendorId, Long contactId);
}
