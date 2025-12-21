```markdown
# PayPal Microservices Backend Application

A **PayPal-like backend system** built using **Spring Boot microservices**, designed to demonstrate real-world backend engineering concepts and **interview-ready system design**.

---

## High-Level Architecture

The system follows a **microservices architecture** with centralized access via an API Gateway.

```

## Tech Stack

- Java 17  
- Spring Boot 3.x  
- Spring Cloud Gateway  
- Spring Security  
- JWT (JJWT)  
- JPA / Hibernate  
- Maven (Multi-module)  
- Docker & Docker Compose  

---
Client
↓
API Gateway
↓
-

## User | Wallet | Transaction | Reward | Notification

```

---

## Services Overview

| Service | Responsibility |
|-------|----------------|
| API Gateway | Routing, JWT validation, rate limiting |
| User Service | User registration, login, roles |
| Wallet Service | Wallet balance management |
| Transaction Service | Money transfers & transactions |
| Reward Service | Reward points calculation |
| Notification Service | Asynchronous notifications |

Each service:
- Runs independently
- Owns its business logic
- Can be scaled separately

---


## Security Design (JWT)

- Stateless authentication using JWT
- API Gateway validates JWT for protected routes
- Public routes (`/auth/**`) bypass authentication
- JWT claims include:
  - `sub` (email)
  - `userId`
  - `role`

---

## API Gateway Responsibilities

- Centralized routing
- JWT authentication
- Rate limiting
- Single entry point for clients

**Rate Limiting Strategy**
- Authenticated requests → `userId`
- Public requests → client IP

---

## Data & Transaction Design

- Each service owns its database schema
- No cross-database joins
- Designed for eventual consistency
- Avoids distributed (2PC) transactions

---

## Project Structure

PayPal-Application/
│
├── api-gateway/
├── user-service/
├── wallet-service/
├── transaction-service/
├── reward-service/
├── notification-service/
│
├── docker-compose.yml
├── pom.xml
└── README.md


## Engineering Highlights

* API Gateway pattern implementation
* JWT-based authentication in microservices
* Rate limiting with fallback strategies
* Service isolation and loose coupling
* Production-aligned Spring Boot practices

---



