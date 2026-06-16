Event-Driven Microservices Order System

A distributed order processing system built using Java, Spring Boot, RabbitMQ, and MongoDB, designed to demonstrate real-world backend engineering concepts such as asynchronous communication, workflow orchestration, retry mechanisms, dead-letter queues, idempotency, and microservices architecture.

Overview

This project simulates the complete lifecycle of an order in a distributed environment.

Instead of relying on direct service-to-service communication, services communicate asynchronously through RabbitMQ events, making the system loosely coupled, scalable, and resilient to failures.

The application consists of four independent microservices:

- Order Service
- Inventory Service
- Payment Service
- Notification Service

Each service owns its own data and communicates through events.

---

Architecture

Client
  │
  ▼
Order Service
  │
  ▼
RabbitMQ
  │
  ▼
Inventory Service
  │
  ▼
InventoryReservedEvent
  │
  ▼
Payment Service
  │
  ▼
PaymentCompletedEvent
  │
  ▼
Notification Service
  │
  ▼
Email Notification

---

Features

Event-Driven Architecture

Services communicate using RabbitMQ events rather than synchronous HTTP calls.

Benefits:

- Loose coupling
- Better scalability
- Independent service evolution
- Improved fault isolation

---

Microservices Architecture

The system is split into independent services:

Service| Responsibility
Order Service| Creates and manages orders
Inventory Service| Validates and reserves stock
Payment Service| Processes payments
Notification Service| Sends order notifications

---

Workflow Sequencing

The project implements sequential event chaining to avoid race conditions.

Successful flow:

CREATED
↓
INVENTORY_RESERVED
↓
PAYMENT_COMPLETED
↓
CONFIRMED

Inventory failure:

CREATED
↓
INVENTORY_FAILED
↓
CANCELLED

Payment failure:

CREATED
↓
INVENTORY_RESERVED
↓
PAYMENT_FAILED
↓
CANCELLED

---

Retry Mechanism

Temporary payment failures are retried automatically.

Benefits:

- Improved reliability
- Better fault tolerance
- Reduced manual intervention

---

Dead Letter Queue (DLQ)

Messages that repeatedly fail processing are moved to a dedicated Dead Letter Queue.

Benefits:

- Prevents infinite retry loops
- Supports debugging and monitoring
- Preserves failed messages for analysis

---

Idempotent Event Processing

The payment service tracks processed event IDs to prevent duplicate payment execution.

Benefits:

- Prevents duplicate transactions
- Handles RabbitMQ at-least-once delivery semantics
- Improves consistency

---

MongoDB Persistence

Each service owns its own database and persists business data independently.

Examples:

- Orders
- Inventory
- Processed Events

Benefits:

- Data durability
- Service autonomy
- Database-per-service pattern

---

Email Notifications

The notification service sends real email notifications using Gmail SMTP after successful order processing.

---

Technologies Used

Backend

- Java
- Spring Boot
- Spring Data MongoDB
- Spring AMQP

Messaging

- RabbitMQ

Database

- MongoDB

Build Tool

- Maven

Testing

- JUnit 5
- Mockito

Email

- Spring Mail
- Gmail SMTP

---

Key Distributed Systems Concepts Demonstrated

Event-Driven Architecture

Services communicate through events.

Publisher-Subscriber Pattern

Services publish and consume events independently.

Workflow Orchestration

Business operations are executed in the correct order through event chaining.

Retry Strategy

Temporary failures are retried automatically.

Dead Letter Queue

Failed events are isolated safely.

Idempotency

Duplicate event processing is prevented.

Distributed Consistency

State transitions are coordinated across services.

Failure Recovery

The system can recover from transient processing failures.

---

Design Patterns Used

- Event-Driven Architecture
- Publisher-Subscriber Pattern
- Repository Pattern
- Dependency Injection
- Layered Architecture
- Message Broker Pattern

---

Project Structure

event-driven-order-system
│
├── order-service
│
├── inventory-service
│
├── payment-service
│
├── notification-service
│
└── documentation

Each service follows a layered architecture:

controller
service
repository
listener
config
model
event

---

Technical Challenges Solved

Race Condition

Initially, Inventory Service and Payment Service consumed the same event simultaneously.

This could lead to:

Payment Success
+
Inventory Failure

Resulting in inconsistent business state.

Solution:

Implemented sequential event chaining:

OrderCreatedEvent
↓
InventoryReservedEvent
↓
PaymentCompletedEvent

---

Duplicate Event Processing

RabbitMQ uses at-least-once delivery.

This means duplicate messages can occur.

Solution:

Implemented idempotency using event IDs and processed-event tracking.

---

Persistent Inventory

Inventory was initially stored in memory.

Problem:

Data was lost on service restart.

Solution:

Migrated inventory management to MongoDB persistence.

---

Future Improvements

- React Dashboard
- Docker Compose Setup
- API Gateway
- JWT Authentication
- Metrics & Monitoring
- Centralized Logging
- Order Analytics Dashboard

---

Learning Outcomes

This project helped build practical experience in:

- Microservices Architecture
- Event-Driven Systems
- RabbitMQ Messaging
- Distributed System Reliability
- MongoDB Persistence
- Spring Boot Development
- Failure Handling Strategies
- System Design Thinking

---

Author

Meet | Vrushil

Backend Developer | Java | Spring Boot | Distributed Systems Enthusiast
