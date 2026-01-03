package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class VendorContactCreationRequest {

    // contact type (PRIMARY, SUPPORT, etc.)
    private String type;

    // email address
    private String email;

    // phone number without country code
    private String phoneNumber;

    // phone country code (e.g., +91)
    private String phoneCode;

    // country id
    private Long countryId;

    // culture / locale id
    private Long cultureId;

    // business hours (day -> time range)
    private Map<String, String> businessHours;
}
