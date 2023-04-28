package com.example.backend.controllers;

import com.example.backend.model.Products;
import com.example.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Products> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<Products> createProduct(@RequestBody Products products) {
        return productService.createProduct(products);
    }

   @PutMapping
    public ResponseEntity<Products> updateProduct(@RequestBody Products products) {
        return productService.updateProduct(products);
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id){
        return productService.deleteProduct(id);
   }
}
