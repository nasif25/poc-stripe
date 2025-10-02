package com.stripe.poc.service;

import com.stripe.poc.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing product tiers and pricing
 * 
 * Handles the 4 pricing tiers: 50, 100, 200, 300 users
 */
@Service
public class ProductService {

    private final List<Product> products;

    public ProductService() {
        // Initialize the 4 pricing tiers as defined in requirements
        this.products = Arrays.asList(
            new Product(
                "price_50_users",
                "Starter Plan",
                "Perfect for small teams getting started",
                5000L, // $50.00 in cents
                "usd",
                50,
                new String[]{"Up to 50 users", "Basic support", "Standard features", "Email integration"}
            ),
            new Product(
                "price_100_users", 
                "Growth Plan",
                "Ideal for growing teams and businesses",
                9000L, // $90.00 in cents
                "usd",
                100,
                new String[]{"Up to 100 users", "Priority support", "Advanced features", "API access", "Custom integrations"}
            ),
            new Product(
                "price_200_users",
                "Professional Plan", 
                "For established teams requiring scale",
                16000L, // $160.00 in cents
                "usd",
                200,
                new String[]{"Up to 200 users", "Premium support", "All features", "Advanced analytics", "Custom branding", "SSO integration"}
            ),
            new Product(
                "price_300_users",
                "Enterprise Plan",
                "Maximum capacity for large organizations", 
                22000L, // $220.00 in cents
                "usd",
                300,
                new String[]{"Up to 300 users", "24/7 dedicated support", "Enterprise features", "Advanced security", "Custom development", "SLA guarantee"}
            )
        );
    }

    /**
     * Get all available products/pricing tiers
     */
    public List<Product> getAllProducts() {
        return products;
    }

    /**
     * Find product by ID
     */
    public Optional<Product> getProductById(String productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    /**
     * Get product by user count
     */
    public Optional<Product> getProductByUserCount(Integer userCount) {
        return products.stream()
                .filter(product -> product.getUsers().equals(userCount))
                .findFirst();
    }

    /**
     * Validate if product exists and is available
     */
    public boolean isValidProduct(String productId) {
        return getProductById(productId).isPresent();
    }

    /**
     * Get product price in cents
     */
    public Long getProductPrice(String productId) {
        return getProductById(productId)
                .map(Product::getPrice)
                .orElse(0L);
    }
}

