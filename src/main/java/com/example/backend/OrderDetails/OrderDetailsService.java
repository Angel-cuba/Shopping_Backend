package com.example.backend.OrderDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderDetailsService {
    @Autowired
    private OrderDetailsRepository repository;

    public List<OrderDetails> saveOrderDetails(List<OrderDetails> orderDetails) {
        return repository.saveAll(orderDetails);
    }

    public List<UUID> getOrderDetailsIdsByUserId(UUID userId) {
        List<OrderDetails> orderDetails = repository.findOrderDetailsByUserId(userId);
        List<UUID> orderDetailsIds = new ArrayList<>();

        for (OrderDetails orderDetail : orderDetails) {
            orderDetailsIds.add(orderDetail.getId());
        }
        return orderDetailsIds;
    }

    public List<OrderDetails> getAllOrderDetailsByIds(UUID[] orderDetailsIds) {
        List<UUID> ids = Arrays.asList(orderDetailsIds);
        return repository.findAllById(ids);
    }

}
