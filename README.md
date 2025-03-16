# 💰 Crypto Wallet Platform

This project is a backend system for tracking cryptocurrency assets in user wallets.
It allows users to manage their assets, keep them updated with real-time prices, and evaluate the wallet’s performance
based on historical data.

---

## 🧱 Architecture & Domain Model

Principles with a focus on aggregates and modularity.

### 🧠 Domain Aggregates

- **User Aggregate**
    - Root entity: `User`
    - A user is uniquely identified by their email and **owns a single Wallet**.
    - In future iterations, the `User` aggregate could evolve to contain more behaviors and permissions.

- **Wallet Aggregate**
    - Root entity: `Wallet`
    - Contains multiple `Asset` entities.
    - Responsible for adding/updating assets, tracking prices, and performing wallet evaluations.

- **Asset**: Represents a crypto token held in the wallet.
- **TokenPrice**: Value Object storing current or historical token price.
- **TokenInfo**: Metadata about tokens (symbol, asset ID) fetched from CoinCap.
- **TokenEvaluationDate**: Value Object that encapsulates the reference date for wallet evaluation logic. Defaults to
  current date when not provided.

📌 *Only Aggregate Roots (`User` and `Wallet`) are responsible for maintaining consistency and lifecycle of their
internals.*

---
## 🧭 Architecture Overview

The diagram below summarizes the domain model, use cases, and infrastructure boundaries:

<img src="crypto-wallet-api/src/main/resources/static/arch_diagram.png" alt="Architecture Diagram" style="width: 80%; max-width: 900px;" />


---

## 🧠 Project Structure

The project root is `crypto-wallet-platform`, designed to host multiple services in the future.

### 🧩 Subproject: `crypto-wallet-api`

This is the main backend application. It follows a modular architecture:

```
crypto-wallet-platform/
└── crypto-wallet-api/
    ├── application/      # Use Cases
    │   └── wallet/
    │       ├── create/
    │       ├── addAsset/
    │       ├── evaluate/
    │       └── get/
    ├── domain/           # Entities, Aggregates, Gateways, Exceptions
    │   ├── user/
    │   └── wallet/
    ├── infrastructure/   # External Interfaces
    │   ├── controller/
    │   ├── configurations/
    │   ├── job/
    │   ├── handler/
    │   ├── restclient/
    │   └── persistence/
    └── CryptoWalletApiApplication.java
```

This separation allows future splitting into proper Gradle/Maven modules (e.g., `domain`, `app`, `infra`, `web`).

---

## 🧠 Application Layer

This layer coordinates the business logic by implementing **use cases** based on domain entities and services.
Each use case is encapsulated in its own class, promoting single responsibility and clear orchestration.

### 📦 Use Cases

Located in `application/wallet`, the following use cases are implemented:

- `WalletCreatorUseCase`:  
  Handles user creation and automatically creates a wallet upon registration.
  Enforces the rule that one user can only have one wallet.

- `AddAssetToWalletUserCase`:  
  Adds or updates a token (asset) in the user's wallet.
  It fetches the token price and persists both the price and the updated wallet.

- `EvaluateWalletUseCase`:  
  Evaluates the wallet's total value either for the current date or a specified historical date.
  Returns the total value and performance metrics.

- `GetWalletUseCase`:  
  Retrieves wallet information by wallet ID.

All use cases extend from the common abstract class `UseCase<IN, OUT>`, which defines a clear input/output contract,
improving testability and reusability.

## 🚀 Features

### ✅ Wallet Creation

- One wallet per user (identified by email).
- Prevents duplicate users.

### ✅ Add Asset

- Add or update an asset in the wallet.
- Price is fetched in real-time and persisted.

### ✅ Token Price Tracking

- Scheduled background job to update token prices.
- Concurrency: fetches prices for 3 tokens at a time.
- Refresh interval is configurable.

### ✅ Wallet Evaluation

- Evaluate wallet value for current or **past** date.
- Performance metrics:
    - Total USD value
    - Best/Worst performing token
    - Percentage variation

### ✅ Data Persistence

- H2 (in-memory) database by default.
- Domain models mapped via Spring Data JPA.

---

## 📦 Tech Stack

| Area             | Tech                              |
|------------------|-----------------------------------|
| Language         | Java 21                           |
| Framework        | Spring Boot 3.2                   |
| Migration        | Flyway                            |
| HTTP Client      | Spring RestClient (uses CoinCap)  |
| Scheduling       | `@Scheduled` + configurable delay |
| Concurrency      | `ExecutorService` (fixed pool)    |
| Persistence      | Spring Data JPA (H2)              |
| Unit Test        | JUnit 5, Mockito                  |
| Integration test | Test Containers                   |

---

## 🔄 API Endpoints

| Method | Endpoint                         | Description                              |
|--------|----------------------------------|------------------------------------------|
| POST   | `/api/wallets`                   | Create wallet for a new user             |
| PATCH  | `/api/wallets/{walletId}/assets` | Add or update an asset in the wallet     |
| POST   | `/api/wallets/evaluation`        | Evaluate wallet at current or given date |

All requests/responses are in JSON format.

---

## 🛠️ How to Run

```bash
# Clone the project
git clone https://github.com/your-repo/crypto-wallet-platform.git

# Navigate to the app folder
cd crypto-wallet-platform/crypto-wallet-api

# Run with Maven
./mvnw spring-boot:run
```

Then access the API using Postman or curl.

---

## 🧪 Testing

- Unit tests written for domain and application layers.
- `TokenPriceUpdaterJobUnitTest` checks job logic and concurrency.
- Use cases tested with mocked gateways.

---

## 🔮 Future Improvements

| Area             | Enhancement Ideas                                 |
|------------------|---------------------------------------------------|
| 🧪 Testing       | Add integration + system tests                    |
| 🐳 Docker        | Add Docker Compose for app + PostgreSQL           |
| 🧾 Swagger       | Add OpenAPI (Swagger UI) for API documentation    |
| 🗃️ Persistence  | Switch to PostgreSQL for production-like setup    |
| ⏱ Scheduling     | Replace `@Scheduled` with Quartz Scheduler        |
| 🔐 Auth          | Add authentication (e.g., OAuth2 / JWT)           |
| 📊 Observability | Integrate Prometheus, Grafana for metrics/logs    |
| 🚀 Multi-Module  | Extract `domain`, `app`, `infra` as separate jars |

---

## 🧾 Challenge Requirements Covered

✅ Evaluate wallet based on token price at any given date  
✅ Limit concurrency to 3 threads when updating prices  
✅ Use CoinCap for token prices  
✅ Return clean, structured JSON  
✅ Clean code, test coverage, and modular design

---

## 🧑‍💻 Author

Developed with ❤️ by **Ronaldo Guastalli**