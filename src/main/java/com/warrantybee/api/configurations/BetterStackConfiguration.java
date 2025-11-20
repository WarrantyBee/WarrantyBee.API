package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class holding properties required for integrating with
 * Better Stack logging services.
 */
@Getter
@Setter
public class BetterStackConfiguration {

    /** The ingestion host URL for sending logs to Better Stack. */
    private String host;

    /** The access token used for authentication with the Better Stack service. */
    private String accessToken;
}
