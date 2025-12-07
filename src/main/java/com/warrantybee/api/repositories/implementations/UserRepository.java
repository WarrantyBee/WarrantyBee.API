package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.internal.*;
import com.warrantybee.api.dto.request.ProfileUpdateRequest;
import com.warrantybee.api.dto.response.*;
import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.SecurityRole;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
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
            List<List<Object[]>> results = new ArrayList<>();
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_RegisterUser");

            query.registerStoredProcedureParameter("in_firstname", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_lastname", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_email", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_password", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("in_accepted_tnc", Boolean.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_accepted_pp", Boolean.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_phone_code", String.class, jakarta.persistence.ParameterMode.IN);
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
            query.registerStoredProcedureParameter("in_culture_id", Long.class, ParameterMode.IN);

            query.setParameter("in_firstname", request.getFirstname());
            query.setParameter("in_lastname", request.getLastname());
            query.setParameter("in_email", request.getEmail());
            query.setParameter("in_password", request.getPassword());
            query.setParameter("in_accepted_tnc", request.getHasAcceptedTnC());
            query.setParameter("in_accepted_pp", request.getHasAcceptedPrivacyPolicy());
            query.setParameter("in_phone_code", request.getPhoneCode());
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
            query.setParameter("in_culture_id", request.getCultureId());

            query.execute();

            do {
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = query.getResultList();
                results.add(resultList);
            } while (query.hasMoreResults());

            if (results.getLast().isEmpty() || results.getLast().get(0)[0] == null) {
                return null;
            }

            return ((Number) results.getLast().get(0)[0]).longValue();
        } catch (Exception e) {
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
            profile.setPhoneCode((String) row[43]);
            profile.setPhoneNumber((String) row[7]);

            if (row[8] != null) {
                profile.setGender(((Number) row[8]).byteValue());
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

            TimeZoneResponse timezone = new TimeZoneResponse();
            timezone.setId((row[21] instanceof Number) ? ((Number) row[21]).longValue() : null);
            timezone.setName((String) row[22]);
            timezone.setAbbreviation((String) row[23]);
            timezone.setOffsetMinutes((row[24] instanceof Number) ? ((Number) row[24]).shortValue() : null);
            timezone.setDst(Boolean.valueOf(String.valueOf(row[25])));
            timezone.setCurrentOffsetMinutes((row[26] instanceof Number) ? ((Number) row[26]).shortValue() : null);

            RegionResponse region = new RegionResponse();
            region.setId((row[18] instanceof Number) ? ((Number) row[18]).longValue() : null);
            region.setName((String) row[19]);
            region.setIso((String) row[20]);
            region.setTimezoneId(timezone.getId());
            address.setRegion(region);

            CurrencyResponse currency = new CurrencyResponse();
            currency.setId((row[27] instanceof Number) ? ((Number) row[27]).longValue() : null);
            currency.setName((String) row[28]);
            currency.setCode((String) row[29]);
            currency.setIso((String) row[30]);
            currency.setSymbol((String) row[31]);
            currency.setMinorUnit((row[32] instanceof Number) ? ((Number) row[32]).byteValue() : null);

            UserSettingsResponse settings = new UserSettingsResponse();
            settings.setIs2FAEnabled(Boolean.valueOf(String.valueOf(row[33])));
            settings.setPasswordUpdatedAt((Timestamp) row[35]);

            CultureResponse culture = new CultureResponse();
            culture.setId((row[36] instanceof Number) ? ((Number) row[36]).longValue() : null);
            culture.setIso((String) row[37]);
            culture.setRtl(Boolean.valueOf(String.valueOf(row[38])));

            LanguageResponse lang = new LanguageResponse();
            lang.setId((row[39] instanceof Number) ? ((Number) row[39]).longValue() : null);
            lang.setName((String) row[40]);
            lang.setIso((String) row[41]);
            lang.setNativeName((String) row[42]);
            culture.setLanguage(lang);

            UserAuthorization authorizationContext = new UserAuthorization();
            authorizationContext.setRole(SecurityRole.getValue((String) row[44]));
            String permission = (String) row[45];
            if (!Validator.isBlank(permission)) {
                String[] permissions = permission.split(",");
                for (String perm : permissions) {
                    authorizationContext.addPermission(SecurityPermission.getValue(perm));
                }
            }

            profile.setAddress(address);
            profile.setTimezone(timezone);
            profile.setCurrency(currency);
            profile.setCulture(culture);
            profile.setSettings(settings);
            userResponse.setProfile(profile);
            userResponse.setAuthorizationContext(authorizationContext);
            userResponse.setPassword((String)row[34]);

            return userResponse;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean store(com.warrantybee.api.dto.internal.LoginTokenDetails details) {
        try {
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_StoreLoginToken");

            query.registerStoredProcedureParameter("in_user_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_token", String.class, ParameterMode.IN);

            query.setParameter("in_user_id", details.getUserId().intValue());
            query.setParameter("in_token", details.getToken());

            query.execute();

            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();

            if (resultList.isEmpty() || ((Number) resultList.get(0)[0]).intValue() != 0) {
                return false;
            }

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean validate(LoginTokenDetails details) {
        try {
            Query query = _entityManager.createNativeQuery(
                    "SELECT ufn_ValidateLoginToken(:userId, :token)"
            );
            query.setParameter("userId", details.getUserId());
            query.setParameter("token", details.getToken());

            Object result = query.getSingleResult();
            return result != null && ((Boolean) result);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean resetPassword(PasswordResetRequest request) {
        try {
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_ResetPassword");

            query.registerStoredProcedureParameter("in_user_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_new_password", String.class, ParameterMode.IN);

            query.setParameter("in_user_id", request.getUserId().intValue());
            query.setParameter("in_new_password", request.getNewPassword());

            query.execute();

            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();

            if (resultList.isEmpty()) {
                return false;
            }

            Object[] row = resultList.get(0);
            int status = ((Number) row[0]).intValue();
            String message = (row[1] != null) ? row[1].toString() : null;

            return status == 0; // Success
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getPasswords(Long id) {
        StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_GetUserPasswords");

        query.registerStoredProcedureParameter(
                "in_user_id",
                Long.class,
                ParameterMode.IN
        );

        query.setParameter("in_user_id", id);

        query.execute();

        @SuppressWarnings("unchecked")
        List<Object> resultList = query.getResultList();
        List<String> passwords = new ArrayList<>();

        for (Object o : resultList) {
            passwords.add((String) o);
        }

        return passwords;
    }

    @Override
    @Transactional
    public Boolean updateProfile(ProfileUpdateRequest request) {
        try {
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_UpdateUserProfile");

            query.registerStoredProcedureParameter("in_user_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_address_line1", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_address_line2", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_phone_code", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_phone_number", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_country_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_region_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_culture_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_city", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_postal_code", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_avatar_url", String.class, ParameterMode.IN);

            query.setParameter("in_user_id", request.getUserId());
            query.setParameter("in_address_line1", request.getAddressLine1());
            query.setParameter("in_address_line2", request.getAddressLine2());
            query.setParameter("in_phone_code", request.getPhoneCode());
            query.setParameter("in_phone_number", request.getPhoneNumber());
            query.setParameter("in_country_id", request.getCountryId());
            query.setParameter("in_region_id", request.getRegionId());
            query.setParameter("in_culture_id", request.getCultureId());
            query.setParameter("in_city", request.getCity());
            query.setParameter("in_postal_code", request.getPostalCode());
            query.setParameter("in_avatar_url", request.getAvatarUrl());

            query.execute();

            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();

            if (resultList.isEmpty()) {
                return false;
            }

            Object[] row = resultList.get(0);
            int status = ((Number) row[0]).intValue();
            return status == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
