package com.example.backend.Products;

import com.example.backend.WishesList.WishesIdsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RestController
@RequestMapping("/api/v1/products")
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

    @GetMapping("/ids")
    public ResponseEntity<List<Products>> getProductsByIds(@RequestBody WishesIdsRequest ids) {
        List<UUID> idsList = ids.getIds();
        return productService.getProductsByIds(idsList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<Products> createProduct(@RequestBody Products products) {
        return productService.createProduct(products);
    }

    @PutMapping
    public Products updateProduct(@RequestBody Products products) {
        return productService.updateProduct(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        return productService.deleteProduct(id);
    }
}
