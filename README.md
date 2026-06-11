# STRIDE Shoe Store — Backend

Spring Boot 3 REST API for the STRIDE Shoe Store.

**Production:** https://shopping-bhjf.onrender.com/api/v1  
**Frontend:** https://starlit-bienenstitch-282c7d.netlify.app

---

## Stack

| Layer | Tech |
|-------|------|
| Framework | Spring Boot 3.0.6 |
| Language | Java 17 |
| Database | PostgreSQL (dev: port 5433) |
| Security | Spring Security 6 + JWT (JJWT 0.11.5) |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway 9.5.1 (`baseline-on-migrate=true`) |
| Build | Maven Wrapper (`./mvnw`) |

---

## Getting Started (Local Dev)

**Requirements:** Java 17, PostgreSQL with a `shopping_db` database.

```bash
# 1. Create the database (first time only)
psql -U postgres -p 5433 -c "CREATE USER shopping_user WITH PASSWORD 'shopping123';"
psql -U postgres -p 5433 -c "CREATE DATABASE shopping_db OWNER shopping_user;"

# 2. Run (Flyway migrations apply automatically on startup)
./mvnw spring-boot:run

# 3. (Optional) Seed sample data
psql -U shopping_user -d shopping_db -p 5433 -f src/main/resources/db/seed.sql
```

Server starts on **port 8080**. CORS allows `localhost:3000`, `localhost:3001`, and the Netlify production URL.

### Environment (dev profile)

Credentials are set in `src/main/resources/application-dev.properties`. Flyway baseline lets the app adopt an existing schema without losing data.

To switch to production profile:
```bash
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

---

## API Reference

All paths are prefixed `/api/v1`.

### Auth
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/users/signup` | — | Register new user |
| POST | `/users/signin` | — | Login — returns JWT |

### Products
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/products` | — | List all products |
| GET | `/products/{id}` | — | Get product by ID |
| POST | `/products` | ADMIN | Create product |
| PUT | `/products` | ADMIN | Update product |
| PUT | `/products/update/stock` | ADMIN | Bulk stock update |
| DELETE | `/products/{id}` | ADMIN | Delete product |

### Orders
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/orders` | ADMIN | All orders (JOIN FETCH user, ordered by date desc) |
| **POST** | **`/orders/place`** | USER | **Atomic order** — validates stock, decrements, creates order+details in one `@Transactional` |
| GET | `/orders/{userId}` | USER | Orders for a user |
| PUT | `/orders/{id}/status` | ADMIN | Update order status |
| ~~POST~~ | ~~`/orders`~~ | ADMIN | **Deprecated** — use `/orders/place` |

### Order Details
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/order-details/{userId}` | USER | Order detail IDs for a user |
| GET | `/order-details/all-order-details?orderDetailsIds=` | USER | Fetch multiple details by IDs (max 100) |
| ~~POST~~ | ~~`/order-details/create-order-details`~~ | ADMIN | **Deprecated** — use `/orders/place` |

### Users
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/users` | ADMIN | All users |
| GET | `/users/{id}` | USER | Get user by ID |
| PUT | `/users/{id}` | USER | Update user (own account only) |
| DELETE | `/users/{id}` | USER | Delete user (own account only) |

### Addresses
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/addresses` | ADMIN | All addresses |
| GET | `/addresses/{userId}` | USER | User's addresses |
| POST | `/addresses` | USER | Create address |
| PUT | `/addresses` | USER | Update address |
| DELETE | `/addresses/{id}` | USER | Delete address |

### Payment Methods
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/payments` | ADMIN | All payment methods |
| GET | `/payments/{userId}` | USER | User's payment methods |
| POST | `/payments` | USER | Add payment method |
| DELETE | `/payments/{id}` | USER | Delete payment method |

### Wishlist
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/wishes` | ADMIN | All wishlists |
| GET | `/wishes/{userId}` | USER | User's wishlist |
| POST | `/wishes` | USER | Create/add to wishlist (IDOR guard: 403 if userId ≠ principal) |
| PUT | `/wishes/{id}` | USER | Update wishlist (IDOR guard) |
| DELETE | `/wishes/{id}` | USER | Delete wishlist (IDOR guard) |

---

## Security Model

| Rule | Detail |
|------|--------|
| JWT filter | `OncePerRequestFilter` — validates every request before controller |
| Public routes | `GET /products/**`, `/users/signup`, `/users/signin` |
| ADMIN-only | `GET /orders`, `GET /users`, `GET /payments`, `GET /wishes` (lists), all product mutations, order status update |
| IDOR guards | `WishesController` — 403 if authenticated user ≠ resource owner (admin bypass) |
| Deprecated endpoints | `POST /orders` and `POST /order-details/create-order-details` restricted to ADMIN |

---

## Data & Migrations

Flyway manages schema evolution. On first startup against an existing schema, Flyway baselines at `V0` and applies pending migrations.

| Migration | Description |
|-----------|-------------|
| V0 (baseline) | Existing schema — orders, order_details, products, users, address, payment_method, wishes_list |
| V1 | `order_details.order_id UUID FK` + backfill from `orders.order_details text[]` + `address.state VARCHAR` |

Seed data (20 products with Unsplash images, 5 users with BCrypt passwords):
```bash
psql -U shopping_user -d shopping_db -p 5433 -f src/main/resources/db/seed.sql
```

---

## Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| `AdminOrderDTO` | Flat record (`Order + User` fields) to avoid `LazyInitializationException` from `@ManyToOne(fetch=LAZY)`. Repository uses `JOIN FETCH`. |
| `Order.orderDetails text[]` | Kept for API backward compatibility. `@OneToMany details` (with `@JsonIgnore`) is the proper relational side. |
| `POST /orders/place` three-phase | Phase 1: validate all stock before any write. Phase 2: save Order first to get ID for FK. Phase 3: `saveAll()` OrderDetails linked to saved Order. |
| `@ToString/@EqualsAndHashCode(exclude)` | Prevents Lombok `@Data` from traversing `@OneToMany` collection in `toString()` / `equals()` (avoids LazyInit + infinite loop). |
| `baseline-on-migrate=true` | Allows Flyway to adopt an existing DB without requiring a clean schema. `ddl-auto=validate` in all profiles — Flyway owns DDL. |
| Constructor injection | All services use constructor injection for testability (`@InjectMocks` without Spring context). |

---

## Tests

```bash
# Run specific test classes (BackendApplicationTests requires a live DB — skip it)
./mvnw test -Dtest="OrderServiceTest,OrderControllerTest,WishesControllerTest" -pl .

# All tests (BackendApplicationTests will fail without DB — pre-existing, expected)
./mvnw test
```

Test setup: `@ExtendWith(MockitoExtension.class)` for service tests, `MockMvcBuilders.standaloneSetup()` with `GlobalExceptionHandler` registered for controller tests.

---

## Module Structure

```
src/main/java/com/example/backend/
├── Address/            # Entity, Repo, Service, Controller
├── Authentication/     # Signin/signup controller
├── Category/           # Product category enum/entity
├── Exceptions/         # GlobalExceptionHandler, domain exceptions
├── JwtFilters/         # JwtFilter (OncePerRequestFilter), JwtService
├── OrderDetails/       # Entity (order_id FK), Repo, Service, Controller
├── Orders/             # Order, OrderStatus, PlaceOrderRequest, AdminOrderDTO, Service, Controller
├── PaymentMethod/      # Entity, Repo, Service, Controller
├── Products/           # Entity, Repo, Service, Controller
├── SecurityConfig/     # SecurityFilterChain, CORS, BCrypt
├── User/               # Entity, Repo, Service, Controller
├── Utils/              # SecurityUtils (getAuthenticatedUsername)
└── WishesList/         # Wishes entity, Repo, Service, Controller (IDOR guards)
```
