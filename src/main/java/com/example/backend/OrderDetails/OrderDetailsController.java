package com.example.backend.OrderDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping("api/v1/order-details")
public class OrderDetailsController {

    private final OrderDetailsService service;

    public OrderDetailsController(OrderDetailsService service) {
        this.service = service;
    }

    @PostMapping("/create-order-details")
    public List<OrderDetails> saveOrderDetails(@RequestBody List<OrderDetails> orderDetails) {
        return service.saveOrderDetails(orderDetails);
    }

    @GetMapping("/{userId}")
    public List<UUID> getOrderDetailsByUserId(@PathVariable UUID userId) {
        return service.getOrderDetailsIdsByUserId(userId);
    }

    /**
     * Fetch multiple OrderDetails by their IDs in one call.
     * Returns an empty list when no IDs are provided instead of a 400 parse error.
     */
    private static final int MAX_IDS = 100;

    @GetMapping("/all-order-details")
    public ResponseEntity<List<OrderDetails>> getAllOrderDetailsByIds(
            @RequestParam(required = false) UUID[] orderDetailsIds) {
        if (orderDetailsIds == null || orderDetailsIds.length == 0) {
            return ResponseEntity.ok(List.of());
        }
        if (orderDetailsIds.length > MAX_IDS) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.getAllOrderDetailsByIds(orderDetailsIds));
    }
}
