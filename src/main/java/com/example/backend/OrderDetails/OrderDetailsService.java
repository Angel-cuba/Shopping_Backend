package com.example.backend.OrderDetails;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderDetailsService {

    private final OrderDetailsRepository repository;

    public OrderDetailsService(OrderDetailsRepository repository) {
        this.repository = repository;
    }

    public List<OrderDetails> saveOrderDetails(List<OrderDetails> orderDetails) {
        return repository.saveAll(orderDetails);
    }

    public List<UUID> getOrderDetailsIdsByUserId(UUID userId) {
        return repository.findOrderDetailsByUserId(userId)
                .stream()
                .map(OrderDetails::getId)
                .toList();
    }

    public List<OrderDetails> getAllOrderDetailsByIds(UUID[] orderDetailsIds) {
        return repository.findAllById(Arrays.asList(orderDetailsIds));
    }
}
