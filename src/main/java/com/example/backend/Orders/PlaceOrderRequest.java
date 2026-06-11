package com.example.backend.Orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

/**
 * Unified request for POST /api/v1/orders/place.
 * Combines what was previously three separate frontend API calls
 * (stock decrement + order-details creation + order creation) into one
 * server-side @Transactional operation.
 */
public record PlaceOrderRequest(
    @NotNull  UUID                        userId,
    @NotEmpty List<@Valid OrderItemRequest> items,
    @NotBlank String                       paymentType,
    @NotBlank String                       shippingAddress,
    @NotBlank String                       shippingMethod,
    @NotNull  Integer                      shippingFee,
    @NotNull  Integer                      total
) {
    public record OrderItemRequest(
        @NotNull  UUID    productId,
        @NotBlank String  variant,
        @NotBlank String  image,
        @NotBlank String  size,
        @NotNull  Integer price,
        @Positive Integer quantity
    ) {}
}
