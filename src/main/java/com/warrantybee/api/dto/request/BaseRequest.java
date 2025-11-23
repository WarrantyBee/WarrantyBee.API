package com.warrantybee.api.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Base request model containing common fields shared across API requests.
 * Provides CAPTCHA response support for validating user actions.
 */
@Getter
@Setter
public abstract class BaseRequest {

    /**
     * The CAPTCHA response token used to verify that the request
     * is initiated by a human user.
     */
    private String captchaResponse;
}
