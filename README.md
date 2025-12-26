# PayPal Application – Microservices Backend

A **microservices-based backend system** that simulates core features of a PayPal-like payment platform.  
The project is built using **Java and Spring Boot**, following real-world **distributed system design principles** such as API Gateway routing, service isolation, and centralized configuration.

---

## 🧩 Project Overview

This application demonstrates how a modern payment platform can be designed using **independently deployable microservices**.  
Each service is responsible for a specific business capability and communicates via REST APIs.

The system is designed for **scalability, maintainability, and clear separation of concerns**, making it suitable for learning and interview-level discussion on microservices architecture.

---


## 🛠️ Tech Stack

- **Language:** Java  
- **Framework:** Spring Boot  
- **Architecture:** Microservices  
- **API Communication:** REST  
- **Containerization:** Docker & Docker Compose  
- **Build Tool:** Maven  
- **Configuration:** application.yml / application.properties  

---

## 🏗️ Architecture

The project follows a **microservices architecture** with the following components:

- **API Gateway**
  - Acts as a single entry point for all client requests
  - Handles request routing and cross-cutting concerns

- **User Service**
  - Manages user registration and authentication
  - Stores user profile information

- **Wallet Service**
  - Maintains user wallet balances
  - Handles credit and debit operations

- **Transaction Service**
  - Processes payment transactions
  - Maintains transaction history

- **Reward Service**
  - Implements reward or loyalty point logic based on transactions

- **Notification Service**
  - Sends notifications related to user actions or transactions

Each service runs independently and can be scaled separately.

---

## 🚀 Key Features

- Microservices-based system design
- API Gateway pattern
- Clear service-to-service responsibility boundaries
- Dockerized environment for easy local setup
- Real-world payment domain modeling

---

## Engineering Highlights

* API Gateway pattern implementation
* JWT-based authentication in microservices
* Rate limiting with fallback strategies
* Service isolation and loose coupling
* Production-aligned Spring Boot practices

## ▶️ Running the Project

### Prerequisites
- Java 17+
- Maven
- Docker & Docker Compose

### Steps
```bash
git clone https://github.com/Nirank/PayPal-Application.git
cd PayPal-Application
docker-compose up
