package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class for defining connection details for the Upstash Redis service.
 */
@Getter
@Setter
public class UpstashConfiguration {

    /** The base URL for the Upstash Redis REST API. */
    private String host;

    /** The access token used for authentication with the Redis API. */
    private String accessToken;
}
