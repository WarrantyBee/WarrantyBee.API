package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds Better Stack logging configuration details.
 */
@Getter
@Setter
public class BetterStackConfiguration {

    /** Better Stack ingesting host URL. */
    private String host;

    /** Access token for Better Stack authentication. */
    private String accessToken;
}
