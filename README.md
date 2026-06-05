# Shopping Backend

Spring Boot 3 REST API for the STRIDE Shoe Store.

**Live:** https://shopping-bhjf.onrender.com/api/v1

---

## Stack

| Layer | Tech |
|-------|------|
| Framework | Spring Boot 3.x |
| Language | Java 17 |
| Database | PostgreSQL |
| Security | Spring Security 6 + JWT (JJWT) |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |

---

## Getting Started

**Prerequisites:** Java 17, PostgreSQL running locally.

```bash
# 1. Create database
psql -U postgres -c "CREATE DATABASE shopping;"

# 2. Configure environment variables (or application.properties)
export DB_URL=jdbc:postgresql://localhost:5432/shopping
export DB_USER=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET=your_very_long_secret_key_at_least_256_bits

# 3. Run
./mvnw spring-boot:run
```

Server starts on **port 8080**. CORS is configured for `http://localhost:3000`.

---

## API Endpoints

All endpoints are prefixed `/api/v1`.

### Auth
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/auth/login` | — | Returns JWT |
| POST | `/users/register` | — | Create account |

### Products
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/products` | — | List all products |
| GET | `/products/{id}` | — | Get product by ID |
| POST | `/products` | ADMIN | Create product |
| PUT | `/products/{id}` | ADMIN | Update product |
| DELETE | `/products/{id}` | ADMIN | Delete product |
| PUT | `/products/update/stock` | USER | Decrement stock on purchase |

### Orders
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/orders` | ADMIN | All orders (with user info) |
| POST | `/orders` | USER | Create order |
| GET | `/orders/user/{userId}` | USER | Orders for a user |
| PUT | `/orders/{id}/status` | ADMIN | Update order status |

### Order Details
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/order-details/create-order-details` | USER | Create order line items |

### Users
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/users` | ADMIN | All users |
| GET | `/users/{id}` | USER | Get user by ID |

### Addresses
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/addresses/user/{userId}` | USER | User's addresses |
| POST | `/addresses` | USER | Create address |
| PUT | `/addresses/{id}` | USER | Update address |
| DELETE | `/addresses/{id}` | USER | Delete address |

### Payment Methods
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/payments/user/{userId}` | USER | User's payment methods |
| POST | `/payments` | USER | Add payment method |
| DELETE | `/payments/{id}` | USER | Delete payment method |

### Wishlist
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/wishes/user/{userId}` | USER | User's wishlist |
| POST | `/wishes` | USER | Add to wishlist |
| DELETE | `/wishes/{id}` | USER | Remove from wishlist |

---

## Security

- JWT tokens are validated on every request via `JwtFilter` (extends `OncePerRequestFilter`)
- Public routes: `/api/v1/auth/**`, GET `/api/v1/products/**`
- ADMIN-only routes: GET `/api/v1/orders`, PUT `/api/v1/orders/*/status`, POST/PUT/DELETE `/api/v1/products/**`, GET `/api/v1/users`
- Role stored as `ROLE_ADMIN` / `ROLE_USER` in the database

---

## Key Design Decisions

- **`AdminOrderDTO`** — flat record mapping `Order + User` fields to avoid `LazyInitializationException` from `@ManyToOne(fetch=LAZY)`. Repository uses `JOIN FETCH` query.
- **`OrderStatus`** — enum: `PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`
- **PathPatternParser** (Spring 6 default) — all `@RequestMapping` paths require a leading `/`
