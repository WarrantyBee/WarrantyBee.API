package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties required for connecting to the Cloudinary service.
 */
@Getter
@Setter
public class CloudinaryConfiguration {

    /**
     * The Cloudinary cloud name associated with the account.
     */
    private String cloud;

    /**
     * The Cloudinary API key used for authentication.
     */
    private String apiKey;

    /**
     * The Cloudinary API secret used for secure API access.
     */
    private String apiSecret;
}
