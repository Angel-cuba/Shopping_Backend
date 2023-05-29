package com.example.backend.Products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Products> getProducts() {
        //Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
        return productRepository.findAll();
    }

    public ResponseEntity<List<Products>> getProductsByIds(List<UUID> ids) {
        List<Products> products = productRepository.findProductsByIdIn(ids);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    public ResponseEntity<Products> getProductById(UUID id) {
        if (!productRepository.existsById(id)) {
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

    public ResponseEntity<Products> updateProduct(Products product) {
        Products productToUpdate = productRepository.findById(product.getId()).orElse(null);
        if (productToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setCategories(product.getCategories());
        productToUpdate.setImage(product.getImage());
        productToUpdate.setVariants(product.getVariants());
        productToUpdate.setSizes(product.getSizes());
        productToUpdate.setInStock(product.getInStock());
        productToUpdate.setPrice(product.getPrice());
        return new ResponseEntity<>(productRepository.save(productToUpdate), HttpStatus.OK);
    }

    public ResponseEntity<Products> updateProductStock(ProductDTO product) {
        Products productToUpdate = productRepository.findById(product.getId()).orElse(null);
        if (productToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (productToUpdate.getInStock() < product.getQuantity()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productToUpdate.setInStock(productToUpdate.getInStock() - product.getQuantity());
        return new ResponseEntity<>(productRepository.save(productToUpdate), HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

