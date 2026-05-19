---
name: warrantybee-api-review
description: >-
  Reviews WarrantyBee.API pull requests for security, stored-proc correctness,
  API contract consistency, and startup code quality. Use for PR review, pre-merge
  checks, or when the user asks to review Java/Spring changes.
---

# WarrantyBee API — Code Review

## Output format

```markdown
## Summary
[1-2 sentences]

## Critical (must fix)
- ...

## Important (should fix)
- ...

## Suggestions
- ...

## Security checklist
- [ ] ...
```

## Critical checks

- [ ] New authenticated routes have `@RequireSecurity` with correct role + **all** required permissions
- [ ] No raw SQL / `JdbcTemplate` bypassing stored procedures
- [ ] Passwords/tokens never logged; Argon2 via `HashHelper`
- [ ] `@RequestBody` present on JSON POST/PATCH
- [ ] JWT claims not trusted from request body for authorization
- [ ] Captcha validated on sensitive mutations
- [ ] New `Error` codes unique; exceptions extend `APIException`

## Contract checks

- [ ] Response uses `APIResponse<T>` shape
- [ ] Breaking changes coordinated with **WarrantyBee.Web** `constants.js`
- [ ] Proc parameter names match Database repo exactly

## Quality checks

- [ ] No god methods; service vs repository boundaries respected
- [ ] Javadoc on new public API methods
- [ ] No unrelated refactors in PR
- [ ] Dependencies justified in `pom.xml`

## Performance

- [ ] No N+1 proc calls in loops
- [ ] Appropriate `@Transactional` scope (not on read-only unless needed)

## Startup bar

Code should be production-minded: explicit exceptions, no TODO without issue link, no commented-out blocks left behind.
