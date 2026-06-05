package com.example.backend.PaymentMethod;

import com.example.backend.User.Role;
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
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService    = userService;
    }

    @GetMapping
    public List<Payment> findAll() {
        return paymentService.findAll();
    }

    @GetMapping("/{id}")
    public Payment findById(@PathVariable UUID id) {
        return paymentService.findById(id);
    }

    /**
     * Returns payment methods for the given userId.
     * Security: only the account owner or an ADMIN can read a user's payment methods.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Payment>> findByUserId(@PathVariable UUID id) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser.getId().equals(id);
        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(paymentService.findPaymentByUserId(id));
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

    private User resolveAuthenticatedUser() {
        String username = SecurityUtils.getAuthenticatedUsername();
        return userService.findUserName(username);
    }
}
