package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a language and its related info.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponse {

    /** The unique identifier of the language. */
    private Long id;

    /** The display name of the language (e.g., "English", "Spanish"). */
    private String name;

    /** The ISO code of the language (e.g., "en" for English). */
    private String iso;

    /** The native name of the language (e.g., "हिन्दी" for Hindi) */
    private String nativeName;
}
