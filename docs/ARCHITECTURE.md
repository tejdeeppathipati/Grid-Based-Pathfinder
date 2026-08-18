# Architecture

The application uses a conventional layered architecture. HTTP and algorithm code remain separate, which keeps the core logic easy to unit test and makes transport changes inexpensive.

```mermaid
flowchart TB
    Browser[React visualizer] --> Proxy[Nginx reverse proxy]
    APIClient[API client / Swagger UI] --> Controllers
    Proxy --> Controllers

    subgraph Spring Boot application
        Controllers[REST controllers<br/>request validation and response mapping]
        Errors[Global exception handler<br/>RFC Problem Details]
        Max[Maximum-score service<br/>dynamic programming]
        Shortest[Shortest-path service<br/>breadth-first search]
        Analysis[Grid-analysis service]
        Saved[Saved-grid and history service]
        Validation[Shared grid validator]
        Domain[Immutable domain records]
        JPA[Spring Data repositories]

        Controllers --> Max
        Controllers --> Shortest
        Controllers --> Analysis
        Controllers --> Saved
        Controllers -. errors .-> Errors
        Max --> Validation
        Shortest --> Validation
        Analysis --> Validation
        Max --> Domain
        Shortest --> Domain
        Analysis --> Domain
        Saved --> JPA
    end

    JPA --> PostgreSQL[(PostgreSQL)]
    Flyway[Flyway migrations] --> PostgreSQL

    Actuator[Actuator health endpoint] --> Client
```

## Package responsibilities

| Package | Responsibility |
| --- | --- |
| `com.gridpathfinder.api` | Versioned HTTP endpoints, request DTOs, Bean Validation, error translation |
| `com.gridpathfinder.service` | Algorithms, grid rules, and use-case orchestration |
| `com.gridpathfinder.domain` | Immutable result and coordinate records |
| `com.gridpathfinder.persistence` | JPA entities and Spring Data repositories |
| `frontend/src` | React grid editor, visualization state, and typed API client |

## Request flow

```mermaid
sequenceDiagram
    participant C as Client
    participant R as REST controller
    participant S as Algorithm service
    participant V as Grid validator

    C->>R: POST JSON request
    R->>R: Bean Validation
    R->>S: Execute use case
    S->>V: Validate grid invariants
    V-->>S: Valid grid
    S->>S: Run DP, BFS, or analysis
    S-->>R: Immutable result record
    R-->>C: 200 JSON response
```

Failures thrown by the service layer are translated centrally into predictable `application/problem+json` responses.

## Testing strategy

```mermaid
flowchart LR
    Unit[Fast service unit tests] --> Verify[mvn verify]
    Web[MockMvc controller slice tests] --> Verify
    Context[Spring context smoke test] --> Verify
    Verify --> CI[GitHub Actions]
```

- Service tests cover algorithm correctness and boundary conditions without starting Spring.
- MockMvc slice tests verify routing, JSON contracts, validation, and status codes.
- The context test catches dependency-wiring and configuration failures.
- CI builds with Java 21 on pushes and pull requests.

## Deliberate scope

Ad-hoc searches remain stateless, while saved grids provide an optional persistence workflow. This keeps algorithms independently testable: persistence orchestrates searches but does not live inside the algorithm implementations. H2 provides a zero-setup local profile; Docker and production use PostgreSQL with the same Flyway-managed schema.
