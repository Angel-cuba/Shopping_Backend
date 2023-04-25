package com.example.backend.services;

import com.example.backend.model.Products;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Products> getProducts() {
        return productRepository.findAll();
    }

    public ResponseEntity<Products> getProductById(Long id) {
        Optional<Products> existingProduct = productRepository.findById(id);

        if(existingProduct.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return existingProduct.map(product -> new ResponseEntity<>(product, HttpStatus.FOUND)).get();
        }
    }

    public ResponseEntity<Products> createProduct(Products products) {
        productRepository.save(products);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }

    public ResponseEntity<Products> updateProduct(Products products) {
        long id = products.getId();
        Optional<Products> existingProductOpcional = productRepository.findById(id);
        if(existingProductOpcional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Products existingProduct = existingProductOpcional.get();
            existingProduct.setName(products.getName());
            existingProduct.setDescription(products.getDescription());
            existingProduct.setImage(products.getImage());
            existingProduct.setCategories(products.getCategories());
            existingProduct.setVariant(products.getVariant());
            existingProduct.setSizes(products.getSizes());
            existingProduct.setPrice(products.getPrice());
            productRepository.save(existingProduct);
            return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
        }

    }

}
