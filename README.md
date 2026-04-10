# 🚀 Webhook Delivery Gateway

A **Spring Boot-based Webhook Delivery System** that reliably receives, stores, and delivers webhook events to configured tenant endpoints with **retry handling, HMAC security, and async processing**.

---

## 📌 System Overview

This project implements a **Webhook Delivery Gateway** that:

1. Accepts webhook events from external producers
2. Validates and stores events
3. Fetches tenant-specific webhook configurations
4. Asynchronously delivers events to target URLs
5. Tracks delivery status and retries failed attempts

---

## ⚙️ Core Architecture

```
External Source
      |
      v
[ Webhook Controller ]
      |
      v
[ Event Service ]
      |
      v
[ Database (WebhookEvent) ]
      |
      v
[ Delivery Service ]
      |
      v
[ HTTP Client → Tenant Webhook URL ]
      |
      v
[ Status Update (SUCCESS / FAILED / RETRY) ]
```

---

## 🔄 Actual Flow (Important)

### 1. Webhook Reception

* Incoming request hits **Webhook Controller**
* Payload mapped into `WebhookEvent`

### 2. Validation Layer

* Tenant lookup via `TenantConfigRepository`
* HMAC signature validation using `HmacUtil`

### 3. Persistence

* Event stored in DB via `WebhookEventRepository`

### 4. Async Processing

* Event pushed for async processing (service layer / executor / queue depending on your setup)

### 5. Delivery Execution

* `DeliveryService` sends HTTP POST to configured endpoint
* Uses `RestTemplate` / `WebClient`

### 6. Retry Mechanism

* Failed deliveries are retried based on:

  * retry count
  * failure status
  * configurable backoff logic (if implemented)

### 7. Status Tracking

* Final status stored in DB:

  * `SUCCESS`
  * `FAILED`
  * `RETRYING`

---

## 📡 API Endpoints (Correct Structure)

### 1.✅ Ingest Webhook Event
```POST /webhooks/ingest```
Request Params:
Parameter	Type	Description
tenantId	String	Tenant identifier
eventType	String	Type of event
eventId	String	Unique event identifier
### Description:

Receives incoming webhook payload for a specific tenant.



---

## 🧩 Key Components

### 📌 Controllers

* `WebhookController`

  * Handles incoming webhook requests

---

### 📌 Services

* `WebhookEventService`

  * Event ingestion + validation
* `DeliveryService`

  * Handles outbound webhook delivery logic
* `TenantConfigService`

  * Fetches endpoint + secret config

---

### 📌 Repositories

* `WebhookEventRepository`
* `TenantConfigRepository`

---

### 📌 Utilities

* `HmacUtil`

  * Generates and validates webhook signatures

---

### 📌 Models

* `WebhookEvent`
* `TenantConfig`
* `WebhookStatus (ENUM)`

---

## 🔐 Security Design

### HMAC Verification

Each webhook request is validated using:

* Shared secret per tenant
* Payload hashing
* Signature comparison via `HmacUtil`

Prevents:

* spoofed requests
* tampered payloads

---

## 🔁 Retry Strategy

Current behavior:

* Failed deliveries are retried
* Retry count tracked in DB
* Status updated after each attempt

Possible enhancements:

* exponential backoff
* dead-letter queue (DLQ)
* circuit breaker

---

## 🧱 Tech Stack

* Java 17+
* Spring Boot
* Spring Web (REST APIs)
* Spring Data JPA
* Lombok
* (Optional) Executor / Async processing
* MySQL / PostgreSQL

---

## 🗂️ Project Structure

```
org.com.webhook.gateway
│
├── controller
├── service
│   ├── WebhookEventService
│   ├── DeliveryService
│   └── TenantConfigService
│
├── repository
├── entity
├── model
├── utils
│   └── HmacUtil
└── config
```

---

## 🚀 How to Run

```bash
git clone https://github.com/avijain09/Webhook-delivery-Gateway.git
cd Webhook-delivery-Gateway
mvn spring-boot:run
```

---

## 📊 Suggested Improvements (Good for README + Resume)

* Kafka-based async delivery pipeline
* Dead-letter queue (DLQ)
* Retry scheduler (Quartz / Spring Scheduler)
* Observability (Prometheus + Grafana)
* Idempotency key handling
* Multi-region webhook delivery

---

## 👨‍💻 Author

**Avi Jain**

