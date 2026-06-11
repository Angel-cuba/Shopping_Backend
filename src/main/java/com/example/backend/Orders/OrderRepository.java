package com.example.backend.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>{
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> findOrdersByUserId(@Param("userId") UUID userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.user ORDER BY o.createdAt DESC")
    List<Order> findAllWithUserOrderByCreatedAtDesc();
}
