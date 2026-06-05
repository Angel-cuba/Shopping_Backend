package com.example.backend.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AdminOrderDTO(
    UUID id,
    List<String> orderDetails,
    String paymentType,
    String shippingAddress,
    String shippingMethod,
    Integer shippingFee,
    Integer total,
    OrderStatus status,
    LocalDateTime createdAt,
    String userFirstname,
    String userLastname,
    String userEmail,
    UUID userId
) {
    public static AdminOrderDTO from(Order o) {
        return new AdminOrderDTO(
            o.getId(), o.getOrderDetails(), o.getPaymentType(),
            o.getShippingAddress(), o.getShippingMethod(),
            o.getShippingFee(), o.getTotal(), o.getStatus(), o.getCreatedAt(),
            o.getUser().getFirstname(), o.getUser().getLastname(), o.getUser().getEmail(),
            o.getUser().getId()
        );
    }
}
