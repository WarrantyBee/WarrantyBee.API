package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.internal.OtpSearchFilter;
import com.warrantybee.api.dto.internal.OtpStorageRequest;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing OTP storage and retrieval operations.
 */
@Repository
public interface IOtpRepository {

    /**
     * Stores the given OTP information.
     *
     * @param request the OTP data to be stored
     * @return the generated record ID
     */
    Long store(OtpStorageRequest request);

    /**
     * Retrieves the stored OTP value for the specified recipient.
     *
     * @param filter the search filter containing recipient details and reason
     * @return the stored OTP value
     */
    String get(OtpSearchFilter filter);
}
