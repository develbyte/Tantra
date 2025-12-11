# Tantra - UI-First DAG Builder

A visual, web-first DAG authoring and config platform where analysts design, test, review, version, and deploy pipelines entirely through UI without git friction.

## Architecture

- **Frontend**: React 19 + TypeScript + Vite (cutting-edge modern UI)
- **Backend**: Java 21 + Spring Boot 3.x
- **Database**: MySQL 8.0+
- **Cache**: Valkey
- **Git**: JGit integration
- **Execution**: Docker/Kubernetes executors

## Project Structure

```
tantra/
├── tantra-backend/     # Spring Boot application
├── tantra-frontend/    # React application
├── tantra-schemas/     # JSON schemas
├── tantra-proto/       # Protobuf definitions
└── README.md
```

## Getting Started

See individual module READMEs for setup instructions.

