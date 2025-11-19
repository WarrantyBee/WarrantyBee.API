package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.internal.OtpSearchFilter;
import com.warrantybee.api.dto.internal.OtpStorageRequest;
import com.warrantybee.api.repositories.interfaces.IOtpRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
        List<List<Object[]>> results = new ArrayList<>();
        StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_GetOtp");
        query.registerStoredProcedureParameter("in_recipient", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("in_recipient_id", Long.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("in_type", Byte.class, jakarta.persistence.ParameterMode.IN);

        query.setParameter("in_recipient", filter.getRecipient());
        query.setParameter("in_recipient_id", filter.getRecipientId());
        query.setParameter("in_type", filter.getReason());
        query.execute();

        do {
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();
            results.add(resultList);
        } while (query.hasMoreResults());

        if (results.size() >= 2 && !results.get(1).isEmpty() &&
            results.get(1).getFirst().length > 2) {
            return (String) results.get(1).get(0)[1];
        }

        return null;
    }
}
