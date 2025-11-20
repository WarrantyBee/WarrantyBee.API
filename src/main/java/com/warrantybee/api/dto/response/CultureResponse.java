package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a culture or locale in the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CultureResponse {

    /** The unique identifier of the culture. */
    private Long id;

    /** The ISO code of the culture (e.g., "en-US", "fr-FR"). */
    private String iso;

    /** Indicates whether the culture's text direction is right-to-left (RTL). */
    private Boolean rtl;

    /** The primary language associated with this culture. */
    private LanguageResponse language;
}
