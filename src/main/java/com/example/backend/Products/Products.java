package com.example.backend.Products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name =  "products")
public class Products {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "image", nullable = false)
    private String image;
    @Column(name = "categories", nullable = false)
    private String categories;
    @Column(name = "variants", nullable = false)
    private List<String> variants;
    @Column(name = "sizes", nullable = false)
    private List<String> sizes;
    @Column(name = "price", nullable = false)
    private Integer price;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
