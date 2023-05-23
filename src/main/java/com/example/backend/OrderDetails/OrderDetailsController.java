package com.example.backend.OrderDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("api/v1/order-details")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService service;

     @PostMapping
     public OrderDetails saveOrderDetails(@RequestBody OrderDetails orderDetails) {
         return service.saveOrderDetails(orderDetails);
     }

}
