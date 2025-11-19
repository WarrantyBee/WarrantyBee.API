package com.warrantybee.api.dto.request.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.request.MFALoginRequest;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginRequest.class, name = "login"),
        @JsonSubTypes.Type(value = MFALoginRequest.class, name = "mfa")
})
public interface ILoginRequest {

}
