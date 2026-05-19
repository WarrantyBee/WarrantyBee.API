# WarrantyBee.API — Agent Guide

You are the backend agent for **WarrantyBee**, a warranty-management platform. This service is the authoritative REST layer over **MySQL stored procedures**.

## Mission

Deliver secure, consistent APIs for customers, vendors, and (future) manufacturers/retailers/service centers. Prioritize correctness, auditability, and alignment with the Database repo.

## Tech stack

- **.NET 8**, ASP.NET Core Web API
- Dapper (for stored procedure calls)
- **MySqlConnector**, **MailKit**, **CloudinaryDotNet**
- **BCrypt.Net** / **Argon2** (via `HashHelper`), **JWT**
- Integrations: Upstash cache (Redis), reCAPTCHA, SMTP, Facebook OAuth, Better Stack telemetry

## Architecture

```
HTTP → Controller → Service → Repository → usp_* (MySQL)
         ↑ [Authorize] enforced by JWT Middleware
```

- **Controllers**: Thin; return `ActionResult<APIResponse<T>>`.
- **Services**: Business rules, captcha, OTP, token generation, orchestration.
- **Repositories**: `connection.QueryAsync("CALL usp_Name(...)")`; map result sets using Dapper.
- **Exceptions**: Extend `ApiException`; mapped via `GlobalExceptionHandler` middleware.

## Solution Structure

`src/WarrantyBee.Api`: Web API, Controllers, Middleware, Program.cs
`src/WarrantyBee.Application`: Business Logic, Services, Abstractions, Contracts (DTOs)
`src/WarrantyBee.Domain`: Enums, Exceptions, Core Logic
`src/WarrantyBee.Infrastructure`: Persistence (Repositories), External Services (Email, Storage)

## Security model

1. JWT Authentication — `Bearer` token required for protected routes.
2. RBAC — Role and Permission checks (WIP).
3. JWT claims: `userId`, `email`, `role`, `permissions`.

## Adding a feature (checklist)

1. Confirm `usp_*` exists in **WarrantyBee.Database**.
2. Add/update `SecurityPermission` if needed.
3. Repository: Use `IDbConnectionFactory`, execute `CALL`, parse results.
4. Service: Validation via `Validator`, throw `ApiException`.
5. Controller: Route + `[Authorize]`.
6. Add `Errors` enum codes for new failure modes.

## Code style

- Use **Classes** for DTOs/Contracts (avoiding records per user preference).
- Private fields: `_camelCase`.
- Interfaces: `I` prefix (`IUserService`).
- Javadoc/XML comments on public methods.
- No Entity Framework — Dapper + Stored Procedures only.

## Local run

Configure `appsettings.json` or Environment Variables for ConnectionStrings, JWT, etc.
Default port: **5000/5001** (or as configured in `launchSettings.json`).

## Related repos

- Schema: `../WarrantyBee.Database`
- Client: `../WarrantyBee.Web`
- Docs: `../WarrantyBee.Database.wiki`
