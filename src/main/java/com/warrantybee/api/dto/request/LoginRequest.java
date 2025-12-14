package com.warrantybee.api.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a generic login request containing the common fields and
 * behaviors shared across different types of authentication flows.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleLoginRequest.class, name = "simple"),
    @JsonSubTypes.Type(value = MFALoginRequest.class, name = "mfa")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest extends BaseRequest {
    /** The user's email address, which serves as the login identifier. */
    private String email;
}
