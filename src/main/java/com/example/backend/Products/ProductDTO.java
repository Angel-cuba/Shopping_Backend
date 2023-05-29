package com.example.backend.Products;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDTO {
    private UUID id;
    private Integer quantity;

    public ProductDTO(UUID id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public ProductDTO() {
    }

    public UUID getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
