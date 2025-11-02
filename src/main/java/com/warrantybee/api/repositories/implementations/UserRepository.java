package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.models.User;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link IUserRepository} for handling user database operations.
 */
@Repository
public class UserRepository implements IUserRepository {

    /** Provides access to JPA entity operations. */
    @PersistenceContext
    private EntityManager _entityManager;

    /** {@inheritDoc}*/
    @Override
    public Long create(UserCreationRequest request) {
        try {
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_RegisterUser");
            query.registerStoredProcedureParameter("in_firstname", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_lastname", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_email", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_password", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_phone_number", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_gender", Byte.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_date_of_birth", java.time.LocalDate.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_address_line1", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_address_line2", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_country_id", Long.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_region_id", Long.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_city", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_postal_code", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_avatar_url", String.class, jakarta.persistence.ParameterMode.IN);

            query.setParameter("in_firstname", request.getFirstname());
            query.setParameter("in_lastname", request.getLastname());
            query.setParameter("in_email", request.getEmail());
            query.setParameter("in_password", request.getPassword());
            query.setParameter("in_phone_number", request.getPhoneNumber());
            query.setParameter("in_gender", request.getGender());
            query.setParameter("in_date_of_birth", request.getDob());
            query.setParameter("in_address_line1", request.getAddressLine1());
            query.setParameter("in_address_line2", request.getAddressLine2());
            query.setParameter("in_country_id", request.getCountryId());
            query.setParameter("in_region_id", request.getRegionId());
            query.setParameter("in_city", request.getCity());
            query.setParameter("in_postal_code", request.getPostalCode());
            query.setParameter("in_avatar_url", request.getAvatarUrl());
            query.execute();
            var result = query.getSingleResult();
            return ((Number) result).longValue();
        } catch (Exception e) {
            return null;
        }
    }

    /** {@inheritDoc}*/
    @Override
    public User get(UserSearchFilter filter) {
        try {
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_GetUser", User.class);
            query.registerStoredProcedureParameter("in_id", Long.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_email", String.class, jakarta.persistence.ParameterMode.IN);
            query.setParameter("in_id", filter.getId());
            query.setParameter("in_email", filter.getEmail());
            query.execute();
            var result = query.getSingleResult();
            return (User) result;
        } catch (Exception e) {
            return null;
        }
    }
}
