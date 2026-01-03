package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.dto.request.VendorLoginCreationRequest;
import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.repositories.interfaces.IVendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Repository implementation for vendor contact persistence operations. */
@Repository
public class VendorRepository implements IVendorRepository {
    /** Provides access to JPA entity operations. */
    @PersistenceContext
    private EntityManager _entityManager;

    @Override
    public Long addContact(Long vendorId, VendorContact contact) {
        try {
            List<List<Object[]>> results = new ArrayList<>();
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_AddVendorContact");

            query.registerStoredProcedureParameter("in_vendor_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_type", Byte.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_email", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_phone_code", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_phone_number", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_country_id", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_culture_id", String.class, ParameterMode.IN);

            query.setParameter("in_vendor_id", vendorId);
            query.setParameter("in_type", (byte) contact.getType().intValue());
            query.setParameter("in_email", contact.getEmail());
            query.setParameter("in_phone_code", contact.getPhoneCode());
            query.setParameter("in_phone_number", contact.getPhoneNumber());
            query.setParameter("in_country_id", contact.getCountryId());
            query.setParameter("in_culture_id", contact.getCultureId());

            query.execute();
            do {
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = query.getResultList();
                results.add(resultList);
            } while (query.hasMoreResults());

            if (results.getLast().isEmpty() || results.getLast().get(0)[0] == null) {
                return null;
            }

            return ((Number) results.getLast().getFirst()[0]).longValue();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateContact(Long vendorId, Long contactId, VendorContact contact) {
        StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_UpdateVendorContact");
        query.registerStoredProcedureParameter("in_vendor_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_contact_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_type", Byte.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_phone_code", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_phone_number", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_country_id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("in_culture_id", String.class, ParameterMode.IN);


        query.setParameter("in_vendor_id", vendorId);
        query.setParameter("in_contact_id", contactId);
        query.setParameter("in_type", contact.getType() != null ? (byte) contact.getType().intValue() : null);
        query.setParameter("in_email", contact.getEmail() != null ? contact.getEmail() : null);
        query.setParameter("in_phone_code", contact.getPhoneCode() != null ? contact.getPhoneCode() : null);
        query.setParameter("in_phone_number", contact.getPhoneNumber() != null ? contact.getPhoneNumber() : null);
        query.setParameter("in_country_id", contact.getCountryId() != null ? contact.getCountryId() : null);
        query.setParameter("in_culture_id", contact.getCultureId() != null ? contact.getCultureId() : null);

        query.execute();
    }

    @Override
    @Transactional
    public void removeContact(Long vendorId, Long contactId) {
        final String query = """
            UPDATE tblVendorContacts
            SET void = 1
            WHERE id = :id AND
            vendor_id = :vendor_id;
        """;

        _entityManager.createNativeQuery(query)
                .setParameter("id", contactId)
                .setParameter("vendor_id", vendorId)
                .executeUpdate();
    }

    @Override
    public Long createVendorLogin(Long vendorId, VendorLoginCreationRequest request) {
        try {
            List<List<Object[]>> results = new ArrayList<>();
            StoredProcedureQuery query = _entityManager.createStoredProcedureQuery("usp_CreateVendorLogin");
            query.registerStoredProcedureParameter("in_vendor_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_user_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_password", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("in_enable_2fa", Boolean.class, ParameterMode.IN);
            query.setParameter("in_vendor_id", vendorId);
            query.setParameter("in_user_id", request.getUserId());
            query.setParameter("in_password", request.getPassword());
            query.setParameter("in_enable_2fa", request.getIs2FAEnabled());
            final String permissions = request.getPermissions().stream()
                    .map(SecurityPermission::getValue)
                    .map(SecurityPermission::getName)
                    .collect(Collectors.joining(","));
            query.setParameter("in_permissions", permissions);
            query.execute();

            do {
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = query.getResultList();
                results.add(resultList);
            } while (query.hasMoreResults());

            if (results.getLast().isEmpty() || results.getLast().get(0)[0] == null) {
                return null;
            }

            return ((Number) results.getLast().getFirst()[0]).longValue();
        }
        catch (Exception e) {
            return null;
        }
    }
}
