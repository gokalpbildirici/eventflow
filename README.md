
  <div align="center">
    <h1>EventFlow — Event-Driven Microservice Showcase</h1>
    <p>
      <strong>Spring Boot 4.1 · Java 21 · Kafka · Docker · Prometheus · Grafana</strong>
    </p>
    <p>
      <a href="#features">Özellikler</a> •
      <a href="#architecture">Mimari</a> •
      <a href="#quickstart">Hızlı Başlangıç</a> •
      <a href="#api">API</a> •
      <a href="#tech-stack">Teknoloji</a> •
      <a href="#monitoring">Monitoring</a>
    </p>
    <br>
  </div>

---

## Features

- **Event-Driven Architecture** — 6 microservice, Kafka ile event-driven iletişim
- **Spring Cloud Stream** — Functional programming model (Supplier/Function/Consumer)
- **API Gateway** — Spring Cloud Gateway ile tek giriş noktası
- **Distributed Tracing** — Micrometer + Zipkin ile uçtan uca trace
- **Observability** — Prometheus metrikleri + Grafana dashboard
- **Containerization** — Tüm servisler Docker Compose ile ayağa kalkar
- **CI/CD Ready** — GitHub Actions pipeline hazır
- **Java 21 + Spring Boot 4.1** — En güncel Java ekosistemi

---

## Architecture

```
                    ┌──────────────┐
                    │  Gateway     │ :8080
                    │  Service     │
                    └──────┬───────┘
                           │
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
    ┌────────────┐  ┌────────────┐  ┌────────────┐
    │   Order    │  │  Payment   │  │ Inventory  │
    │  Service   │  │  Service   │  │  Service   │
    │   :8081    │  │   :8082    │  │   :8083    │
    └─────┬──────┘  └─────┬──────┘  └─────┬──────┘
          │               │               │
          └───────────────┼───────────────┘
                          ▼
                   ┌──────────────┐
                   │    Kafka     │
                   │  Event Bus   │
                   └──────┬───────┘
                          │
          ┌───────────────┼───────────────┐
          ▼               ▼               ▼
   ┌────────────┐  ┌────────────┐  ┌────────────┐
   │Notification│  │ Analytics  │  │   Zipkin   │
   │  Service   │  │  Service   │  │  Tracing   │
   │   :8084    │  │   :8086    │  │   :9411    │
   └────────────┘  └────────────┘  └────────────┘
```

### Event Flow

```
POST /api/orders
  → Order Service publish → Kafka [order.events]
    → Payment Service consume → process → publish → Kafka [payment.events]
    → Inventory Service consume → reserve stock → publish → Kafka [inventory.events]
    → Notification Service consume → log notification
    → Analytics Service consume → update metrics
```

---

## Quickstart

### Prerequisites

- Java 21+
- Docker & Docker Compose
- Maven (opsiyonel, Docker build için gerekli değil)

### Run with Docker Compose (recommended)

```bash
# Tüm altyapıyı ayağa kaldır
docker compose up -d

# Servisleri takip et
docker compose logs -f
```

Bu komut şunları başlatır:
| Service | Port | Description |
|---------|------|-------------|
| Gateway | 8080 | API Gateway |
| Order | 8081 | Order management |
| Payment | 8082 | Payment processing |
| Inventory | 8083 | Stock management |
| Notification | 8084 | Event notifications |
| Analytics | 8086 | Business metrics |
| Kafka | 9092 | Event bus |
| Zipkin | 9411 | Distributed tracing |
| Prometheus | 9090 | Metrics collection |
| Grafana | 3000 | Dashboard |

### Run locally (without Docker)

```bash
# Kafka'yı çalıştır (gerekli)
docker compose up -d kafka zookeeper

# Her servisi ayrı terminalde başlat
mvn spring-boot:run -pl order-service
mvn spring-boot:run -pl payment-service
mvn spring-boot:run -pl inventory-service
mvn spring-boot:run -pl notification-service
mvn spring-boot:run -pl analytics-service
mvn spring-boot:run -pl gateway-service
```

---

## API

### Order Service (via Gateway: `localhost:8080`)

```bash
# Sipariş oluştur
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "C001",
    "productId": "P001",
    "quantity": 2,
    "totalAmount": 3000.00
  }'

# Tüm siparişleri listele
curl http://localhost:8080/api/orders

# Sipariş detayı
curl http://localhost:8080/api/orders/{orderId}
```

### Inventory Service (via Gateway: `localhost:8080`)

```bash
# Ürün ekle
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"id": "P005", "name": "Headset", "stock": 50, "price": 150.00}'

# Tüm ürünleri listele
curl http://localhost:8080/api/products
```

### Analytics Service (via Gateway: `localhost:8080`)

```bash
# İş metriklerini görüntüle
curl http://localhost:8080/api/analytics/summary
```

### Observability Endpoints

```bash
# Zipkin (distributed tracing)
http://localhost:9411

# Prometheus (metrics)
http://localhost:9090

# Grafana (dashboard) — admin/admin
http://localhost:3000
```

---

## Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.1 |
| **Event Bus** | Apache Kafka + Zookeeper |
| **API Gateway** | Spring Cloud Gateway |
| **Messaging** | Spring Cloud Stream (Kafka Binder) |
| **Build Tool** | Maven (Multi-Module) |
| **Container** | Docker + Docker Compose |
| **Monitoring** | Prometheus + Grafana |
| **Tracing** | Micrometer + Zipkin |
| **CI/CD** | GitHub Actions |
| **Project Structure** | 7 modules (common, 6 services) |

---

## Modules

| Module | Port | Responsibility |
|--------|------|---------------|
| `common` | — | Shared events, DTOs |
| `gateway-service` | 8080 | API Gateway, request routing |
| `order-service` | 8081 | Order CRUD, publishes OrderCreatedEvent |
| `payment-service` | 8082 | Consumes orders, processes payments |
| `inventory-service` | 8083 | Product CRUD, stock reservation |
| `notification-service` | 8084 | Consumes all events, logs notifications |
| `analytics-service` | 8086 | Consumes all events, exposes metrics |

---

## Monitoring

The system includes full observability stack:

1. **Distributed Tracing**: Zipkin UI at `http://localhost:9411` — her servis arası isteği trace eder
2. **Metrics**: Prometheus scrapes all services at `/actuator/prometheus`
3. **Dashboards**: Grafana at `http://localhost:3000` (admin/admin) with pre-configured Prometheus datasource

---

## Project Structure

```
eventflow/
├── pom.xml                          # Parent POM (multi-module)
├── common/                          # Shared events & DTOs
├── order-service/                   # Order management service
├── payment-service/                 # Payment processing service
├── inventory-service/               # Inventory & stock service
├── notification-service/            # Notification service
├── analytics-service/               # Analytics & metrics service
├── gateway-service/                 # API Gateway
├── docker-compose.yml               # Full infrastructure
├── prometheus/                      # Prometheus configuration
├── grafana/                         # Grafana datasources
└── .github/workflows/               # CI/CD pipeline
```

---

## License

MIT License — see [LICENSE](LICENSE)

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/gokalpbildirici">Gokalp Bildirici</a>
</p>
