# ARCHITECTURE INTENT

## Clean Architecture Structure

- domain/
  - Core business rules, entities, value objects, domain services.
- application/
  - Use cases, input ports, output ports.
- infrastructure/
  - Adapters (web, persistence, messaging), framework wiring.

## Import Rules

- domain must not import Spring, Jackson, Reactor, JPA.
- application must not depend on infrastructure.
- infrastructure may depend on all layers.

## Mandatory Patterns

- Repository Pattern via domain ports (application/ports/out).
- Strategy Pattern for variable behavior (no large switch-case in domain/services).

## Architecture Decision

// ARCHITECTURE_DECISION:
This interface exists to decouple persistence and enable mocking in unit tests.

## Why We Are Refactoring

This refactor aligns the codebase with the workshop Definition of Done (DoD)
by enforcing layer boundaries, reducing framework coupling in core logic, and
improving unit testability without changing production behavior.
