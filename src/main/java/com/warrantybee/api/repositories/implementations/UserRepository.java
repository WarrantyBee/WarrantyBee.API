package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.response.*;
import com.warrantybee.api.enumerations.Gender;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Implementation of {@link IUserRepository} for handling user database operations.
 */
@Repository
public class UserRepository implements IUserRepository {

    /** Provides access to JPA entity operations. */
    @PersistenceContext
    private EntityManager _entityManager;

    @Override
    @Transactional
    public Long create(UserCreationRequest request) {
        try {
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_RegisterUser");

            query.registerStoredProcedureParameter("in_firstname", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_lastname", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_email", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_password", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_phone_number", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_gender", Byte.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_date_of_birth", java.sql.Date.class, jakarta.persistence.ParameterMode.IN);
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
            query.setParameter("in_date_of_birth", (request.getDateOfBirth() != null) ? java.sql.Date.valueOf(request.getDateOfBirth()) : null);
            query.setParameter("in_address_line1", request.getAddressLine1());
            query.setParameter("in_address_line2", request.getAddressLine2());
            query.setParameter("in_country_id", request.getCountryId());
            query.setParameter("in_region_id", request.getRegionId());
            query.setParameter("in_city", request.getCity());
            query.setParameter("in_postal_code", request.getPostalCode());
            query.setParameter("in_avatar_url", request.getAvatarUrl());

            query.execute();

            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();

            if (resultList.isEmpty() || resultList.get(0)[0] == null) {
                return null;
            }

            return ((Number) resultList.get(0)[0]).longValue();
        } catch (Exception e) {
            System.err.println("Error during user creation: " + e.getMessage());
            return null;
        }
    }

    @Override
    public UserResponse get(UserSearchFilter filter) {
        try {
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_GetUser");

            query.registerStoredProcedureParameter("in_id", Long.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_email", String.class, jakarta.persistence.ParameterMode.IN);

            query.setParameter("in_id", filter.getId());
            query.setParameter("in_email", filter.getEmail());

            query.execute();

            @SuppressWarnings("unchecked")
            List<Object[]> statusResult = query.getResultList();

            if (statusResult.isEmpty() || ((Number) statusResult.get(0)[0]).intValue() != 0) {
                return null;
            }

            if (!query.hasMoreResults()) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();

            if (results.isEmpty()) {
                return null;
            }

            Object[] row = results.get(0);
            UserResponse userResponse = new UserResponse();
            userResponse.setId(((Number) row[0]).longValue());
            userResponse.setFirstname((String) row[2]);
            userResponse.setLastname((String) row[3]);
            userResponse.setEmail((String) row[4]);

            UserProfileResponse profile = new UserProfileResponse();
            profile.setPhoneNumber((String) row[7]);

            if (row[8] != null) {
                Byte genderValue = ((Number) row[8]).byteValue();
                profile.setGender(Gender.values()[genderValue - 1]);
            } else {
                profile.setGender(null);
            }

            profile.setDateOfBirth((row[9] instanceof java.sql.Date) ? ((java.sql.Date) row[9]).toLocalDate() : null);
            profile.setAvatarUrl((String) row[14]);

            UserAddressResponse address = new UserAddressResponse();
            address.setAddressLine1((String) row[10]);
            address.setAddressLine2((String) row[11]);
            address.setCity((String) row[12]);
            address.setPostalCode((String) row[13]);

            CountryResponse country = new CountryResponse();
            country.setId((row[15] instanceof Number) ? ((Number) row[15]).longValue() : null);
            country.setName((String) row[16]);
            country.setIso3((String) row[17]);
            address.setCountry(country);

            RegionResponse region = new RegionResponse();
            region.setId((row[18] instanceof Number) ? ((Number) row[18]).longValue() : null);
            region.setName((String) row[19]);
            region.setIso((String) row[20]);
            address.setRegion(region);

            profile.setAddress(address);
            userResponse.setDetails(profile);

            return userResponse;

        } catch (Exception e) {
            return null;
        }
    }
}
