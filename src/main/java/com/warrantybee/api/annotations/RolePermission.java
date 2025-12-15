package com.warrantybee.api.annotations;

import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.SecurityRole;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a role and its required permissions for accessing an endpoint.
 * Typically used inside {@link RequireSecurity} to specify multiple role-permission sets.
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolePermission {

    /** Role required to access the endpoint */
    SecurityRole role();

    /** Permissions required for the role; user must have all listed */
    SecurityPermission[] permissions() default {};
}
