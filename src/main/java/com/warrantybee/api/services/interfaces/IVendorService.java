package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.internal.VendorContact;

/**
 * Defines operations related to vendor management within the system.
 */
public interface IVendorService {

    /**
     * Updates an existing vendor contact or creates a new one if it does not already exist.
     * @param contact the vendor contact details to be updated
     */
    void updateContact(VendorContact contact);

    /**
     * Removes a vendor contact from the system using its unique identifier.
     * @param vendorContactId the unique identifier of the vendor contact to be removed
     */
    void removeContact(Integer vendorContactId);
}
