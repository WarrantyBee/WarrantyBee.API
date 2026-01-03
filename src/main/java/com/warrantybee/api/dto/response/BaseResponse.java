package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base response object containing a unique identifier.
 * <p> Intended to be extended by response DTOs that require an ID reference. </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    /** The unique identifier associated with the response. */
    private Long id;
}
