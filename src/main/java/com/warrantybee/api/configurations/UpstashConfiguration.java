package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Upstash Redis configuration.
 */
@Getter
@Setter
public class UpstashConfiguration {

    /** Redis REST URL. */
    private String host;

    /** Access token for authentication. */
    private String accessToken;
}
