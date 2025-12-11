# Tantra Backend

Java 21 Spring Boot backend for Tantra DAG Builder.

## Tech Stack

- Java 21
- Spring Boot 3.2.0
- MySQL 8.0+
- Valkey (Redis-compatible)
- JGit for git operations
- Flyway for database migrations

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8.0+
- Valkey (or Redis)

### Setup

1. Create MySQL database:
```sql
CREATE DATABASE tantra CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Update `application.yml` with your database credentials

3. Run the application:
```bash
mvn spring-boot:run
```

The API will be available at http://localhost:8080

## API Endpoints

### Pipelines
- `POST /api/v1/pipelines` - Create pipeline
- `GET /api/v1/pipelines/{id}` - Get pipeline
- `PUT /api/v1/pipelines/{id}` - Update pipeline
- `DELETE /api/v1/pipelines/{id}` - Delete pipeline
- `POST /api/v1/pipelines/{id}/validate` - Validate pipeline
- `GET /api/v1/pipelines/{id}/versions` - List versions

### Runs
- `POST /api/v1/runs/pipelines/{pipelineId}/run` - Trigger run
- `GET /api/v1/runs/{runId}` - Get run status
- `GET /api/v1/runs/{runId}/logs` - Stream logs (SSE)
- `GET /api/v1/runs` - List runs

### Git
- `POST /api/v1/git/commit` - Commit pipeline changes
- `GET /api/v1/git/versions/{pipelineId}` - List git versions
- `POST /api/v1/git/diff` - Get diff between versions
- `POST /api/v1/git/rollback` - Rollback to version

