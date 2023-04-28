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
       if(!productRepository.existsById(id)){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);

       } else {
           Optional<Products> product = productRepository.findById(id);
           return new ResponseEntity<>(product.get(), HttpStatus.OK);
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

    public ResponseEntity<Void> deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

