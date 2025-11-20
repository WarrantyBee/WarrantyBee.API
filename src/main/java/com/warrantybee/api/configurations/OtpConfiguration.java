package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class for handling OTP (One-Time Password) related settings.
 */
@Getter
@Setter
public class OtpConfiguration {

    /**
     * The expiration time of the OTP, typically defined in minutes,
     * after which the OTP becomes invalid.
     */
    private Integer expiration;
}
