package com.warrantybee.api.dto.response;

import com.warrantybee.api.dto.response.interfaces.ILoginResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the response returned after an MFA-based login.
 */
@Getter
@Setter
@AllArgsConstructor
public class MFALoginResponse implements ILoginResponse {

    /** Token issued after successful MFA verification. */
    private String loginToken;
}
