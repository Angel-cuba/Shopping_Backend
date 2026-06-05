package com.example.backend.Orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    public Order saveOrder(Order order) {
        return repository.save(order);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return repository.findOrdersByUserId(userId);
    }

    public List<AdminOrderDTO> findAllOrders() {
        return repository.findAllWithUserOrderByCreatedAtDesc()
                .stream()
                .map(AdminOrderDTO::from)
                .toList();
    }

    public AdminOrderDTO updateOrderStatus(UUID id, OrderStatus status) {
        Order order = repository.findById(id).orElse(null);
        if (order == null) return null;
        order.setStatus(status);
        return AdminOrderDTO.from(repository.save(order));
    }
}
