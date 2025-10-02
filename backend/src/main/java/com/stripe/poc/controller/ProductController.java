package com.stripe.poc.controller;

import com.stripe.poc.model.Product;
import com.stripe.poc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Product operations
 * 
 * Handles product listing and retrieval for the pricing tiers
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Get all available products/pricing tiers
     * GET /api/products
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get specific product by ID
     * GET /api/products/{productId}
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        try {
            Optional<Product> product = productService.getProductById(productId);
            if (product.isPresent()) {
                return ResponseEntity.ok(product.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get product by user count
     * GET /api/products/users/{userCount}
     */
    @GetMapping("/products/users/{userCount}")
    public ResponseEntity<Product> getProductByUserCount(@PathVariable Integer userCount) {
        try {
            Optional<Product> product = productService.getProductByUserCount(userCount);
            if (product.isPresent()) {
                return ResponseEntity.ok(product.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

