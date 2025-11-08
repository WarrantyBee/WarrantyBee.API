package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.internal.OtpSearchFilter;
import com.warrantybee.api.dto.internal.OtpStorageRequest;
import com.warrantybee.api.repositories.interfaces.IOtpRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link IOtpRepository} for handling OTP storage and retrieval.
 */
@Repository
public class OtpRepository implements IOtpRepository {

    /** Provides access to JPA entity operations. */
    @PersistenceContext
    private EntityManager _entityManager;

    @Override
    public Long store(OtpStorageRequest request) {
        StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_StoreOtp");
        query.registerStoredProcedureParameter("in_value", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_recipient", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_recipient_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_type", Byte.class, ParameterMode.IN);

        query.setParameter("in_value", request.getValue());
        query.setParameter("in_recipient", request.getRecipient());
        query.setParameter("in_recipient_id", request.getRecipientId());
        query.setParameter("in_type", request.getReason());

        boolean hasResult = query.execute();

        if (hasResult) {
            Object[] result = (Object[]) query.getSingleResult();
            if (result != null && result.length > 0 && result[0] != null) {
                return ((Number) result[0]).longValue();
            }
        }

        return null;
    }

    @Override
    public String get(OtpSearchFilter filter) {
        StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_GetOtp");
        query.registerStoredProcedureParameter("in_recipient", String.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("in_recipient", filter.getRecipient());
        query.setParameter("in_recipient_id", filter.getRecipientId());
        query.setParameter("in_type", filter.getReason());
        boolean hasResult = query.execute();

        if (hasResult) {
            Object[] result = (Object[]) query.getSingleResult();
            if (result.length >= 2 && result[1] != null) {
                return result[1].toString();
            }
        }

        return null;
    }
}
