# 🐾 PetShop

A cloud-native **PetShop application** built to practice and demonstrate modern software engineering, microservices architecture, containerization, Kubernetes, and CI/CD.

The project is built with **Spring Boot**, **Angular**, **Docker**, **Kubernetes**, and **Azure**, with automated testing and deployment through **GitHub Actions**.

---

## 🏗️ Architecture

```text
                         ┌──────────────────┐
                         │     Angular      │
                         │    Frontend      │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │   API Gateway    │
                         └────────┬─────────┘
                                  │
              ┌───────────────────┼───────────────────┐
              │                   │                   │
              ▼                   ▼                   ▼
       ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
       │    Owner    │     │     Pet     │     │    Order    │
       │   Service   │     │   Service   │     │   Service   │
       │ Spring Boot │     │ Spring Boot │     │ Spring Boot │
       └──────┬──────┘     └──────┬──────┘     └──────┬──────┘
              │                   │                   │
              ▼                   ▼                   ▼
       ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
       │  Owner DB   │     │   Pet DB    │     │  Order DB   │
       │ PostgreSQL  │     │ PostgreSQL  │     │ PostgreSQL  │
       └─────────────┘     └─────────────┘     └─────────────┘


                    CI/CD
                      │
                      ▼
                ┌───────────┐
                │  GitHub   │
                │  Actions  │
                └─────┬─────┘
                      │
              Build / Test / Scan
                      │
                      ▼
              ┌───────────────┐
              │ Azure         │
              │ Container     │
              │ Registry      │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │ Azure         │
              │ Kubernetes    │
              │ Service (AKS) │
              └───────────────┘
```

The application follows the **database-per-service** approach: each microservice owns its data and database rather than sharing a single database.

---

## 🚀 Goals

This project is primarily a practical learning project focused on:

- Building a real microservices architecture
- Developing REST APIs with Spring Boot
- Building a frontend with Angular
- Containerizing applications with Docker
- Running services locally with Docker Compose
- Writing unit and integration tests
- Using Testcontainers for database integration testing
- Building CI/CD pipelines with GitHub Actions
- Publishing Docker images to Azure Container Registry
- Deploying microservices to Azure Kubernetes Service
- Managing Kubernetes deployments and services
- Learning cloud-native application architecture

---

## 🛠️ Technology Stack

### Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Web
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- Testcontainers

### Frontend

- Angular
- TypeScript
- HTML / CSS

### DevOps

- Docker
- Docker Compose
- Kubernetes
- GitHub Actions
- Azure Container Registry (ACR)
- Azure Kubernetes Service (AKS)

### Code Quality

- SonarQube
- Unit testing
- Integration testing

---

## 📦 Microservices

### Owner Service

Responsible for managing pet owners.

Example responsibilities:

```text
POST   /owners
GET    /owners
GET    /owners/{id}
PUT    /owners/{id}
DELETE /owners/{id}
```

Technology:

```text
Spring Boot
PostgreSQL
Spring Data JPA
Testcontainers
```

### Pet Service

Responsible for managing pets.

Planned responsibilities:

```text
POST   /pets
GET    /pets
GET    /pets/{id}
PUT    /pets/{id}
DELETE /pets/{id}
```

### Order Service

Responsible for managing customer orders.

The service will own its own database and communicate with other services through APIs/events rather than directly accessing their databases.

---

## 🗂️ Repository Structure

```text
petshop/
│
├── owner-service/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   └── test/
│   │       └── java/
│   │
│   ├── Dockerfile
│   ├── pom.xml
│   └── README.md
│
├── pet-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   ├── Dockerfile
│   └── package.json
│
├── docker-compose.yml
│
├── k8s/
│   ├── owner-service/
│   │   ├── deployment.yaml
│   │   └── service.yaml
│   │
│   ├── pet-service/
│   ├── order-service/
│   └── frontend/
│
└── .github/
    └── workflows/
        └── ci-cd.yml
```

---

# 🐳 Local Development

The complete application can be started locally using Docker Compose.

```bash
docker compose up -d
```

Check running containers:

```bash
docker ps
```

Stop the environment:

```bash
docker compose down
```

---

# 🧪 Testing

The project uses two levels of testing.

### Unit Tests

Unit tests validate individual components without requiring external infrastructure.

Example:

```text
Service
   ↓
JUnit + Mockito
```

### Integration Tests

Repository integration tests use **Testcontainers** to run a real PostgreSQL instance inside Docker.

Example:

```text
OwnerRepositoryTest
        │
        ▼
   Testcontainers
        │
        ▼
postgres:16-alpine
        │
        ▼
 Spring Data JPA
        │
        ▼
  OwnerRepository
```

Example:

```java
@Testcontainers
@SpringBootTest
class OwnerRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    // tests...
}
```

Spring Boot's `@ServiceConnection` allows the application to automatically obtain connection information from the Testcontainers-managed database.

Run tests:

```bash
mvn test
```

---

# 🔄 CI/CD Pipeline

The project uses GitHub Actions to automate the software delivery process.

```text
                    Git Push
                       │
                       ▼
                GitHub Actions
                       │
              ┌────────┴────────┐
              │                 │
              ▼                 ▼
          Build Code        Run Tests
                                │
                                ▼
                         Testcontainers
                                │
                                ▼
                         PostgreSQL
                                │
                                ▼
                         Integration Tests
                                │
                                ▼
                           SonarQube
                                │
                                ▼
                         Docker Build
                                │
                                ▼
                     Azure Container Registry
                                │
                                ▼
                         Azure Kubernetes
                            Service
                                │
                                ▼
                           Deployment
```

The objective is that a successful change can automatically progress from source code to a tested container image and finally to AKS. GitHub Actions supports building containers, pushing them to ACR, configuring AKS, and deploying them as part of a workflow.

---

# ☁️ Azure Architecture

The production deployment will use:

```text
GitHub
   │
   │ GitHub Actions
   ▼
Azure Container Registry
   │
   │ Docker Images
   ▼
Azure Kubernetes Service
   │
   ├── Owner Service
   ├── Pet Service
   ├── Order Service
   ├── API Gateway
   └── Angular Frontend
```

The AKS cluster will pull application images from ACR.

---

# ☸️ Kubernetes

Each microservice will have its own Kubernetes deployment.

Example:

```text
AKS Cluster
│
├── owner-service
│   ├── Deployment
│   └── Service
│
├── pet-service
│   ├── Deployment
│   └── Service
│
├── order-service
│   ├── Deployment
│   └── Service
│
├── api-gateway
│   ├── Deployment
│   └── Service
│
└── frontend
    ├── Deployment
    └── Service
```

Kubernetes will handle:

- Container scheduling
- Service discovery
- Scaling
- Rolling deployments
- Container restarts
- Configuration

---

# 🔐 Configuration & Secrets

Application configuration will be separated from application code.

Local development:

```text
application-local.yml
Docker Compose
```

Kubernetes:

```text
ConfigMap
Secret
```

Cloud credentials and sensitive values will not be committed to Git.

---

# 📈 Future Improvements

Planned improvements include:

- [ ] Complete Owner Service
- [ ] Complete Pet Service
- [ ] Complete Order Service
- [ ] Add API Gateway
- [ ] Complete Angular frontend
- [ ] Add service-to-service communication
- [ ] Add authentication and authorization
- [ ] Add centralized configuration
- [ ] Add distributed tracing
- [ ] Add centralized logging
- [ ] Add health checks and observability
- [ ] Add SonarQube quality gates
- [ ] Complete Docker Compose environment
- [ ] Complete Kubernetes manifests
- [ ] Create Azure infrastructure
- [ ] Create ACR
- [ ] Create AKS cluster
- [ ] Connect AKS to ACR
- [ ] Implement GitHub Actions CI
- [ ] Implement automated Docker image publishing
- [ ] Implement automated AKS deployment
- [ ] Add deployment verification
- [ ] Add production monitoring

---

# 🎯 Project Objective

PetShop is not intended to be just a CRUD application.

The goal is to build an **end-to-end cloud-native system**, starting from application development and going all the way to automated deployment:

```text
Code
 ↓
Spring Boot / Angular
 ↓
Tests
 ↓
Docker
 ↓
GitHub Actions
 ↓
Azure Container Registry
 ↓
Kubernetes
 ↓
Azure Kubernetes Service
```

The project is designed to provide hands-on experience with the complete lifecycle of a modern microservices application.
