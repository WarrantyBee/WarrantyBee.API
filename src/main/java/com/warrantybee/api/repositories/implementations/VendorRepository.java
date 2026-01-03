package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.repositories.interfaces.IVendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;

public class VendorRepository implements IVendorRepository {
    /** Provides access to JPA entity operations. */
    @PersistenceContext
    private EntityManager _entityManager;

    @Override
    public void updateContact(Long vendorId, Long contactId, VendorContact contact) {
        if (contact != null) {
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
    }

    @Override
    @Transactional
    public void deleteContact(Long vendorId, Long contactId) {
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
}
