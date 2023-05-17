package com.example.backend.PaymentMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<Payment> findAll() {
        return paymentService.findAll();
    }

    @GetMapping("/{id}")
    public Payment findById(@PathVariable UUID id) {
        return paymentService.findById(id);
    }

    @GetMapping("/user/{id}")
    public List<Payment> findByUserId(@PathVariable UUID id) {
        return paymentService.findPaymentsByUserId(id);
    }
    @PostMapping
    public Payment createOne(@RequestBody Payment payment) {
        return paymentService.createOne(payment);
    }

    @PutMapping
    public Payment update(@RequestBody Payment payment) {
        return paymentService.updateOne(payment);
    }

    @DeleteMapping("/{id}")
    public Payment delete(@PathVariable UUID id) {
        return paymentService.deleteById(id);
    }
}
