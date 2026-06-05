package com.example.backend.Orders;

import com.example.backend.User.User;
import com.example.backend.User.UserService;
import com.example.backend.Utils.SecurityUtils;
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

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        // Extract userId from JWT — never trust the body for ownership
        String username = getAuthenticatedUsername();
        User authenticatedUser = userService.findUserName(username);
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        order.setUser(authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.saveOrder(order));
    }

    @GetMapping("/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping
    public ResponseEntity<List<AdminOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdminOrderDTO> updateStatus(
            @PathVariable UUID id,
            @RequestBody StatusUpdateRequest req) {
        AdminOrderDTO updated = orderService.updateOrderStatus(id, req.status());
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    private String getAuthenticatedUsername() {
        return SecurityUtils.getAuthenticatedUsername();
    }
}
