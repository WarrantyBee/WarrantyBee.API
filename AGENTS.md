# WarrantyBee.API — Agent Guide

You are the backend agent for **WarrantyBee**, a warranty-management platform. This service is the authoritative REST layer over **MySQL stored procedures** — not a typical JPA entity application.

## Mission

Deliver secure, consistent APIs for customers, vendors, and (future) manufacturers/retailers/service centers. Prioritize correctness, auditability, and alignment with the Database repo.

## Tech stack

- Java **21**, Spring Boot **3.5.6**
- Spring Web, Security, Validation, Mail, Data JPA (for `EntityManager` + stored procs only)
- **Auth0 java-jwt**, **Argon2** (via `HashHelper`), **MySQL**
- Integrations: Upstash cache, reCAPTCHA, SMTP, Cloudinary, Facebook OAuth, Better Stack telemetry

## Architecture

```
HTTP → Controller → Service → Repository → usp_* (MySQL)
         ↑ @RequireSecurity enforced by SecurityFilter (interceptor)
         ↑ JWT validated by AuthenticationFilter
```

- **Controllers**: thin; return `ResponseEntity<APIResponse<T>>`.
- **Services**: business rules, captcha, OTP, token generation, orchestration.
- **Repositories**: `createStoredProcedureQuery("usp_Name")`; map `Object[]` result sets.
- **Exceptions**: extend `APIException`; map via `GlobalExceptionHandler` + `Error` enum (numeric codes).

## Package layout

`com.warrantybee.api.{controllers,services,.repositories,dto,enumerations,exceptions,config,configurations,helpers,annotations,advices,constants}`

## Security model

1. `AuthenticationFilter` — Bearer JWT required except whitelisted paths (`APP_WHITELISTED_ENDPOINTS`).
2. `SecurityFilter` — `@RequireSecurity` + `@RolePermission(role, permissions[])` on methods.
3. JWT claims: `userId`, `email`, `role`, `permissions` (list of permission names).
4. Vendor login: when `LoginRequest.role == VENDOR`, `AuthService` loads `tblVendorLogins` via `VendorRepository`.

## Adding a feature (checklist)

1. Confirm `usp_*` exists in **WarrantyBee.Database** (or add it there first).
2. Add/update `SecurityPermission` + seed `tblPermissions` / `tblRolePermissions` if needed.
3. Repository: register IN/OUT params, execute, parse result sets.
4. Service: validation via `Validator`, throw domain exceptions.
5. Controller: route + `@RequireSecurity`; use `@RequestBody` for JSON bodies.
6. Add `Error` codes + exception classes for new failure modes.
7. Whitelist public endpoints in env config docs (not committed secrets).

## Code style

- Private fields: `_camelCase` (existing convention).
- Interfaces: `I` prefix (`IUserService`, `IUserRepository`).
- Lombok on DTOs where already used.
- Javadoc on public controller/service methods.
- No Hibernate `@Entity` for domain tables — stored procedures only.

## Do not

- Put business logic in controllers or filters.
- Bypass stored procedures with raw SQL in the API.
- Commit secrets or real `application.properties` values.
- Add JPA entities for `tbl*` tables without team approval.
- Break the `APIResponse` / `APIError` envelope shape.

## Local run

Configure env vars matching `application.properties` keys (`app.data-source-configuration.*`, JWT, SMTP, etc.). Default port **8080**. Health: `POST /api/alive` (authenticated), `GET /api/status`.

## Related repos

- Schema: `../WarrantyBee.Database`
- Client: `../WarrantyBee.Web`
- Docs: `../WarrantyBee.Database.wiki`

## Skills (invoke when relevant)

- `.cursor/skills/warrantybee-api-feature/` — end-to-end API feature workflow
- `.cursor/skills/warrantybee-api-review/` — PR and security review
