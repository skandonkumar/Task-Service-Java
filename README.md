# Task Service - Java (Spring Boot)

High-throughput task management API built with Spring Boot and Apache Cassandra.

## Overview

This service implements the task management API using:
- **Framework**: Spring Boot 3.x
- **Database**: Apache Cassandra (Wide-column NoSQL)
- **Cache**: Redis
- **Purpose**: High write-scale, distributed task storage

## Architecture

```
TaskController → TaskRepository → CassandraTemplate → Cassandra
      ↓
    Redis (Cache Layer)
```

## Prerequisites

- Java 17+
- Maven 3.8+
- Cassandra 4.0+
- Redis 7.0+
- Docker (for containerized deployment)

## Local Development

### 1. Start Dependencies

```bash
# Start Cassandra
docker run -d --name cassandra -p 9042:9042 cassandra:4.0

# Start Redis
docker run -d --name redis -p 6379:6379 redis:alpine

# Wait for Cassandra to be ready (30-60 seconds)
sleep 60
```

### 2. Initialize Cassandra Keyspace

```bash
docker exec -it cassandra cqlsh -e "CREATE KEYSPACE IF NOT EXISTS task_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"
```

### 3. Build and Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The service will start on `http://localhost:8080`

## Configuration

Edit [src/main/resources/application.properties](src/main/resources/application.properties):

```properties
# Server
server.port=8080

# Cassandra
spring.cassandra.contact-points=cassandra-service
spring.cassandra.port=9042
spring.cassandra.keyspace-name=task_keyspace

# Redis
spring.data.redis.host=redis-service
spring.data.redis.port=6379
```

### Environment Variables

Override defaults with environment variables:

- `CASSANDRA_HOST` - Cassandra contact point (default: `cassandra-service`)
- `CASSANDRA_PORT` - Cassandra port (default: `9042`)
- `REDIS_HOST` - Redis host (default: `redis-service`)
- `REDIS_PORT` - Redis port (default: `6379`)

## API Endpoints

### Create Task

```bash
POST /api/java/v1/tasks
Content-Type: application/json

{
  "title": "My Task",
  "description": "Task description"
}
```

### Get Task

```bash
GET /api/java/v1/tasks/{id}
```

### Update Task

```bash
PUT /api/java/v1/tasks/{id}
Content-Type: application/json

{
  "title": "Updated Title",
  "status": "IN_PROGRESS"
}
```

### Delete Task

```bash
DELETE /api/java/v1/tasks/{id}
```

## Docker Deployment

### Build Image

```bash
docker build -t task-service-java:1.0.0 .
```

### Run Container

```bash
docker run -d \
  --name java-backend \
  --network task-network \
  -p 8080:8080 \
  -e CASSANDRA_HOST=cassandra \
  -e REDIS_HOST=redis \
  task-service-java:1.0.0
```

## Kubernetes Deployment

```bash
kubectl apply -f /path/to/infrastructure/dev/java-backend.yaml
```

Monitor startup:

```bash
kubectl get pods -n dev -l app=java-backend -w
kubectl logs -n dev -l app=java-backend
```

## Project Structure

```
src/main/
├── java/com/polyglot/task/
│   ├── TaskServiceApplication.java    # Main Spring Boot app
│   ├── controller/
│   │   └── TaskController.java        # REST endpoints
│   ├── model/
│   │   └── Task.java                  # Entity model
│   ├── repository/
│   │   └── TaskRepository.java        # Data access layer
│   └── config/
│       └── CassandraConfig.java       # Cassandra initialization
└── resources/
    └── application.properties         # Configuration
```

## Testing

Run the integrated tests:

```bash
./mvnw test
```

## Troubleshooting

### Cassandra Connection Refused

Ensure Cassandra container/pod is running and accessible:

```bash
# Docker
docker logs cassandra

# Kubernetes
kubectl logs -n dev -l app=cassandra
```

### Redis Connection Failed

Check Redis service availability:

```bash
# Docker
docker logs redis

# Kubernetes
kubectl logs -n dev -l app=redis
```

### Keyspace Does Not Exist

The keyspace is automatically created on startup via the `CassandraConfig` bean. If it fails:

```bash
# Manual creation
kubectl exec -it -n dev <cassandra-pod> -- cqlsh -e "CREATE KEYSPACE IF NOT EXISTS task_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"
```

## Performance Notes

- Cassandra handles high-write scenarios efficiently
- Redis caching reduces repeated queries
- Wide-column model optimizes for time-series task data

## License

MIT
