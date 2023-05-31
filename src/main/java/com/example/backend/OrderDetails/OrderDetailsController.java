package com.example.backend.OrderDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping("api/v1/order-details")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService service;

    @PostMapping("/create-order-details")
    public List<OrderDetails> saveOrderDetails(@RequestBody List<OrderDetails> orderDetails) {
        return service.saveOrderDetails(orderDetails);
    }

    @GetMapping("/{userId}")
    public List<UUID> getOrderDetailsByUserId(@PathVariable UUID userId) {
        return service.getOrderDetailsIdsByUserId(userId);
    }

    @GetMapping("/all-order-details")
    public List<OrderDetails> getAllOrderDetailsByIds(@RequestBody List<UUID> orderDetailsIds) {
        return service.getAllOrderDetailsByIds(orderDetailsIds);
    }

    @GetMapping("/user")
    public List<OrderDetails> getAllOrderDetailsById(@RequestParam("orderDetailsIds") List<UUID> orderDetailsIds) {
        return service.getAllOrderDetailsById(orderDetailsIds);
    }

}
