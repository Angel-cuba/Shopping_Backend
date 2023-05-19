package com.example.backend.Products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Products, UUID> {
    List<Products> findProductsByIdIn(List<UUID> ids);
}
