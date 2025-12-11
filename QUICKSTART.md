# Tantra Quick Start Guide

## Overview

Tantra is a UI-first, no-code/low-code DAG builder with a modern React frontend and Java 21 Spring Boot backend.

## Architecture

- **Frontend**: React 19 + TypeScript + Vite (port 3000)
- **Backend**: Java 21 + Spring Boot 3.x (port 8080)
- **Database**: MySQL 8.0+
- **Cache**: Valkey (Redis-compatible)

## Prerequisites

- Java 21+
- Node.js 18+
- MySQL 8.0+
- Valkey or Redis
- Maven 3.8+

## Setup Instructions

### 1. Database Setup

```sql
CREATE DATABASE tantra CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Backend Setup

```bash
cd tantra-backend

# Update application.yml with your MySQL and Valkey credentials

# Run migrations (via Flyway on startup)
mvn spring-boot:run
```

Backend will be available at http://localhost:8080

### 3. Frontend Setup

```bash
cd tantra-frontend

npm install
npm run dev
```

Frontend will be available at http://localhost:3000

## Features Implemented

### Phase 1: Foundation ✅
- ✅ Project structure (backend, frontend, schemas)
- ✅ MySQL database schema with all tables
- ✅ Core REST API endpoints
- ✅ DAG JSON schema validation

### Phase 2: Core Canvas ✅
- ✅ React Flow DAG canvas with modern UI
- ✅ Node property panel
- ✅ Backend CRUD operations
- ✅ Pipeline versioning

### API Endpoints

**Pipelines:**
- `POST /api/v1/pipelines` - Create pipeline
- `GET /api/v1/pipelines/{id}` - Get pipeline with versions
- `PUT /api/v1/pipelines/{id}` - Update pipeline
- `DELETE /api/v1/pipelines/{id}` - Delete pipeline
- `POST /api/v1/pipelines/{id}/validate` - Validate pipeline

**Runs:**
- `POST /api/v1/runs/pipelines/{pipelineId}/run` - Trigger run
- `GET /api/v1/runs/{runId}` - Get run status
- `GET /api/v1/runs/{runId}/logs` - Stream logs (SSE)

**Git:**
- `POST /api/v1/git/commit` - Commit pipeline changes
- `GET /api/v1/git/versions/{pipelineId}` - List versions
- `POST /api/v1/git/rollback` - Rollback to version

## Next Steps

- Implement Docker executor for job execution
- Add test run sandbox
- Implement secrets management UI
- Add scheduler UI with cron builder
- Build observability dashboard

## Project Structure

```
tantra/
├── tantra-backend/          # Spring Boot application
│   ├── src/main/java/       # Java source code
│   └── src/main/resources/ # Config & migrations
├── tantra-frontend/         # React application
│   └── src/                # React source code
├── tantra-schemas/         # JSON schemas
└── tantra-proto/           # Protobuf definitions
```

