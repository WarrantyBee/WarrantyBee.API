---
name: warrantybee-api-feature
description: >-
  Implements end-to-end API features for WarrantyBee.API following stored-procedure
  architecture, security annotations, and Error envelope. Use when adding endpoints,
  services, repositories, vendor flows, auth changes, or integrating new usp_* procs.
---

# WarrantyBee API — Feature Workflow

## Prerequisites

- [ ] Stored procedure merged in **WarrantyBee.Database**
- [ ] Permissions/roles seeded if new capability
- [ ] Branch name aligns with DB feature branch when cross-repo

## Step 1 — Repository

1. Add method to `I*Repository` interface.
2. Implement in `*Repository` with `createStoredProcedureQuery("usp_Name")`.
3. Register all `in_*` / `out_*` parameters with correct Java types (`Byte` for TINYINT, `java.sql.Date` for DATE).
4. Parse multiple result sets if proc returns them (follow `UserRepository` patterns).
5. Return domain DTOs or primitives — never leak `Object[]` past repository.

## Step 2 — Service

1. Add to `I*Service` / `*Service`.
2. Call `Validator` for input rules (email, phone, business hours, etc.).
3. Use `IHttpContext` for current user on user-scoped operations.
4. Throw domain exceptions (`UserNotRegisteredException`, etc.) — never return null for errors.

## Step 3 — Controller

1. Map route under `/api/...`.
2. Add `@RequireSecurity` with correct `SecurityRole` + `SecurityPermission[]`.
3. Use `@RequestBody` for JSON request DTOs.
4. Return `ResponseEntity.ok(new APIResponse<>(data, null))` or empty data with null error on success.

## Step 4 — Errors & enums

1. Add `Error` enum entry with unique code (check existing range 1001+).
2. Create `*Exception extends APIException`.
3. Verify `GlobalExceptionHandler` covers it (usually automatic for `APIException`).

## Step 5 — Verify

- [ ] Whitelist updated for any new public route (document in PR, not in repo secrets)
- [ ] Maven compiles: `mvn -q compile`
- [ ] Manual test against local MySQL with migrated schema
- [ ] No secrets in diff

## Vendor-specific

- Vendor login path: ensure `LoginRequest.role` and `_fetchVendorLoginInfo` behavior documented in PR.
- Vendor contacts: validate `VendorContactType`, `BusinessHours` via `Validator` before `usp_AddVendorContact`.

## Reference

- Existing flow: `AuthController` → `AuthService` → `UserRepository` / `VendorRepository`
- Security: `SecurityFilter`, `AuthenticationFilter`
