package com.example.backend.Orders;

import com.example.backend.User.Role;
import com.example.backend.User.User;
import com.example.backend.User.UserService;
import com.example.backend.Utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping(path = "/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * @deprecated Use {@link #placeOrder(PlaceOrderRequest)} instead — it is atomic and
     * validates stock transactionally. This endpoint will be removed in a future sprint.
     * Restricted to ADMIN to prevent accidental use by regular users.
     */
    @Deprecated
    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Only ADMINs can use the legacy non-transactional endpoint (SecurityConfig enforces this)
        order.setUser(authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.saveOrder(order));
    }

    /**
     * Returns orders for the given userId.
     * Security: only the account owner or an ADMIN can read a user's order history.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable UUID userId) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser.getId().equals(userId);
        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<AdminOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    /**
     * Update order status (ADMIN only — enforced by SecurityConfig requestMatcher).
     * NotFoundException from the service propagates to GlobalExceptionHandler → 404.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<AdminOrderDTO> updateStatus(
            @PathVariable UUID id,
            @RequestBody StatusUpdateRequest req) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, req.status()));
    }

    /**
     * Place a complete order transactionally — stock decrement, order-details creation,
     * and order creation all run inside a single DB transaction.
     * Any failure rolls back automatically via @Transactional.
     *
     * Security: the userId in the request body is verified against the authenticated
     * JWT principal — a logged-in user cannot place orders on behalf of another account.
     *
     * Error mapping (GlobalExceptionHandler):
     *   NotFoundException          → 404 (user or product not found)
     *   InsufficientStockException → 409 (cart quantity exceeds available stock)
     */
    @PostMapping("/place")
    public ResponseEntity<AdminOrderDTO> placeOrder(@Valid @RequestBody PlaceOrderRequest req) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!authenticatedUser.getId().equals(req.userId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AdminOrderDTO result = orderService.placeOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // ── helpers ────────────────────────────────────────────────────────────────

    /** Resolves the JWT principal to a User entity, or null if not found. */
    private User resolveAuthenticatedUser() {
        String username = SecurityUtils.getAuthenticatedUsername();
        return userService.findUserName(username);
    }
}
