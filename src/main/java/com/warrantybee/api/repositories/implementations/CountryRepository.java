package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.dto.response.*;
import com.warrantybee.api.repositories.interfaces.ICountryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for accessing country data from the database.
 * Uses {@link EntityManager} for persistence operations.
 */
@Repository
public class CountryRepository implements ICountryRepository {

    /** JPA EntityManager used for database operations. */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<CountryDetailResponse> get() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("usp_GetCountries");
        query.execute();

        List<Object[]> results = query.getResultList();
        List<CountryDetailResponse> countries = new ArrayList<>();

        for (Object[] row : results) {
            CountryDetailResponse country = new CountryDetailResponse();
            country.setId(((Number) row[0]).longValue());
            country.setIso2((String) row[1]);
            country.setIso3((String) row[2]);
            country.setCode((String) row[3]);
            country.setName((String) row[4]);
            country.setOfficialName((String) row[5]);
            country.setCapital((String) row[6]);
            country.setPhoneCode((String) row[7]);

            // Parse currency JSON
            if (row[8] != null) {
                JSONObject currencyJson = new JSONObject((String) row[8]);
                country.setCurrency(new CurrencyResponse(
                        currencyJson.has("id") ? currencyJson.getLong("id") : null,
                        currencyJson.optString("name", null),
                        currencyJson.optString("iso", null),
                        currencyJson.optString("code", null),
                        currencyJson.optString("symbol", null),
                        currencyJson.has("minorUnit") ? (byte) currencyJson.getInt("minorUnit") : null
                ));
            }

            // Parse states JSON
            if (row[9] != null) {
                JSONArray statesJson = new JSONArray((String) row[9]);
                List<RegionResponse> regions = new ArrayList<>();
                for (int i = 0; i < statesJson.length(); i++) {
                    JSONObject stateJson = statesJson.getJSONObject(i);
                    regions.add(new RegionResponse(
                            stateJson.has("id") ? stateJson.getLong("id") : null,
                            stateJson.optString("name", null),
                            stateJson.optString("iso", null),
                            stateJson.optString("capital", null),
                            stateJson.has("timezoneId") ? stateJson.getLong("timezoneId") : null
                    ));
                }
                country.setRegions(regions);
            }

            // Parse cultures JSON
            if (row[10] != null) {
                JSONArray culturesJson = new JSONArray((String) row[10]);
                List<CultureResponse> cultures = new ArrayList<>();
                for (int i = 0; i < culturesJson.length(); i++) {
                    JSONObject cultureJson = culturesJson.getJSONObject(i);
                    JSONObject languageJson = cultureJson.optJSONObject("language");
                    LanguageResponse language = null;
                    if (languageJson != null) {
                        language = new LanguageResponse(
                                languageJson.has("id") ? languageJson.getLong("id") : null,
                                languageJson.optString("name", null),
                                languageJson.optString("iso", null),
                                languageJson.optString("nativeName", null)
                        );
                    }
                    cultures.add(new CultureResponse(
                            cultureJson.has("id") ? cultureJson.getLong("id") : null,
                            cultureJson.optString("iso", null),
                            cultureJson.optBoolean("rtl"),
                            language
                    ));
                }
                country.setCultures(cultures);
            }

            // Parse timezones JSON
            if (row[11] != null) {
                JSONArray timezonesJson = new JSONArray((String) row[11]);
                List<TimeZoneResponse> timezones = new ArrayList<>();
                for (int i = 0; i < timezonesJson.length(); i++) {
                    JSONObject timezoneJson = timezonesJson.getJSONObject(i);
                    timezones.add(new TimeZoneResponse(
                            timezoneJson.has("id") ? timezoneJson.getLong("id") : null,
                            timezoneJson.optString("name", null),
                            timezoneJson.optString("abbreviation", null),
                            timezoneJson.has("offsetMinutes") ? (short) timezoneJson.getInt("offsetMinutes") : null,
                            timezoneJson.optBoolean("dst"),
                            timezoneJson.has("currentOffsetMinutes") ? (short) timezoneJson.getInt("currentOffsetMinutes") : null
                    ));
                }
                country.setTimezones(timezones);
            }

            countries.add(country);
        }

        return countries;
    }
}
