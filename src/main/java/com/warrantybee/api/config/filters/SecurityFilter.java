package com.warrantybee.api.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrantybee.api.annotations.RequireSecurity;
import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.enumerations.Error;
import com.warrantybee.api.services.interfaces.IHttpContext;
import com.warrantybee.api.enumerations.SecurityRole;
import com.warrantybee.api.enumerations.SecurityPermission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * Filter that enforces security based on {@link RequireSecurity} annotations.
 */
@Component
public class SecurityFilter implements HandlerInterceptor {

    private final IHttpContext _httpContext;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Constructs a SecurityFilter with the given HTTP context service.
     *
     * @param httpContext the service providing current user info, roles, and permissions
     */
    public SecurityFilter(IHttpContext httpContext) {
        this._httpContext = httpContext;
    }

    /**
     * Intercepts the request before it reaches the controller.
     *
     * @param request  the current HTTP request
     * @param response the current HTTP response
     * @param handler  the handler (or controller method) to be executed
     * @return {@code true} if the request should proceed; {@code false} if access is denied
     * @throws Exception if an error occurs during permission checking or response writing
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod method)) return true;

        RequireSecurity annotation = method.getMethodAnnotation(RequireSecurity.class);
        if (annotation == null) {
            annotation = method.getBeanType().getAnnotation(RequireSecurity.class);
        }

        if (annotation != null) {
            _httpContext.initialize();
            SecurityRole userRole = _httpContext.getRole();
            Set<SecurityPermission> userPermissions = new HashSet<>(_httpContext.getPermissions());

            boolean allowed = Arrays.stream(annotation.value())
                    .anyMatch(rp -> rp.role() == userRole &&
                            Arrays.stream(rp.permissions()).allMatch(userPermissions::contains));

            if (!allowed) {
                response.setStatus(Error.REQUIRE_SECURITY.getStatus().value());
                response.getWriter().write(
                    objectMapper.writeValueAsString(
                        new APIResponse<>(null,
                            new APIError(
                                Error.REQUIRE_SECURITY.getCode(),
                                Error.REQUIRE_SECURITY.getMessage()
                            )
                        )
                    )
                );
                return false;
            }
        }

        return true;
    }
}
