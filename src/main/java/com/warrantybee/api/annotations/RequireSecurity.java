package com.warrantybee.api.annotations;

import java.lang.annotation.*;

/**
 * Specifies the role-permission sets required to access a method or class.
 * Apply this to controller methods or classes to enforce security based on roles and permissions.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireSecurity {

    /** Array of role-permission mappings defining access rules */
    RolePermission[] value();
}
