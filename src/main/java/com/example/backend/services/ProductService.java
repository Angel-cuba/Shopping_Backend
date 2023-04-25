package com.example.backend.services;

import com.example.backend.model.Products;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Products> getProducts() {
        return productRepository.findAll();
    }

}
