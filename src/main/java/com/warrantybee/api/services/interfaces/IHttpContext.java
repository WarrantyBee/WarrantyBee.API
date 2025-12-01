package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.SecurityRole;

import java.util.List;

/**
 * Service interface for accessing HTTP context-related information
 * from the current request.
 */
public interface IHttpContext {

    /**
     * Initializes the context and populates context info.
     * */
    void initialize();

    /**
     * Gets the unique identifier of the currently authenticated user.
     *
     * @return the user ID as a {@link Long}, or {@code null} if no user is authenticated
     */
    Long getUserId();

    /**
     * Gets the email address of the currently authenticated user.
     *
     * @return the user's email as a {@link String}, or {@code null} if no user is authenticated
     */
    String getEmail();

    /**
     * Gets the access token associated with the current HTTP request.
     *
     * @return the access token as a {@link String}, or {@code null} if no token is present
     */
    String getAccessToken();

    /**
     * Gets the security role assigned to the currently authenticated user.
     *
     * @return the user's {@link SecurityRole}, or {@code null} if no user is authenticated
     */
    SecurityRole getRole();

    /**
     * Gets the list of security permissions granted to the currently authenticated user.
     *
     * @return a {@link List} of {@link SecurityPermission}, or an empty list if no permissions are assigned
     */
    List<SecurityPermission> getPermissions();
}
