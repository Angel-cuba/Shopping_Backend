package com.example.backend.Orders;

import java.util.List;
import java.util.UUID;

/**
 * Unified request for POST /api/v1/orders/place.
 * Combines what was previously three separate frontend API calls
 * (stock decrement + order-details creation + order creation) into one
 * server-side @Transactional operation.
 */
public record PlaceOrderRequest(
    UUID userId,
    List<OrderItemRequest> items,
    String paymentType,
    String shippingAddress,
    String shippingMethod,
    Integer shippingFee,
    Integer total
) {
    public record OrderItemRequest(
        UUID   productId,
        String variant,
        String image,
        String size,
        Integer price,
        Integer quantity
    ) {}
}
