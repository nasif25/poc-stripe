package com.stripe.poc.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.poc.config.StripeConfig;
import com.stripe.poc.model.PaymentRequest;
import com.stripe.poc.model.PaymentResponse;
import com.stripe.poc.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for handling Stripe payment operations
 * 
 * Manages PaymentIntent creation, status checking, and Stripe API interactions
 */
@Service
public class StripeService {

    @Autowired
    private ProductService productService;

    @Autowired
    private StripeConfig stripeConfig;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    /**
     * Create a PaymentIntent for the specified product
     */
    public PaymentResponse createPaymentIntent(PaymentRequest paymentRequest) throws StripeException {
        // Validate product exists
        Optional<Product> productOpt = productService.getProductById(paymentRequest.getProductId());
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid product ID: " + paymentRequest.getProductId());
        }

        Product product = productOpt.get();
        
        // Verify amount matches product price
        if (!paymentRequest.getAmount().equals(product.getPrice())) {
            throw new IllegalArgumentException("Amount mismatch for product: " + paymentRequest.getProductId());
        }

        // Create metadata for tracking
        Map<String, String> metadata = new HashMap<>();
        metadata.put("product_id", product.getId());
        metadata.put("product_name", product.getName());
        metadata.put("user_count", product.getUsers().toString());
        if (paymentRequest.getCustomerEmail() != null) {
            metadata.put("customer_email", paymentRequest.getCustomerEmail());
        }
        if (paymentRequest.getCustomerName() != null) {
            metadata.put("customer_name", paymentRequest.getCustomerName());
        }

        // Create PaymentIntent parameters
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentRequest.getAmount())
                .setCurrency(paymentRequest.getCurrency())
                .setDescription("Payment for " + product.getName() + " (" + product.getUsers() + " users)")
                .putAllMetadata(metadata)
                // Enable test mode automatic confirmation for demo
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC)
                .build();

        try {
            // Create the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Create response
            PaymentResponse response = new PaymentResponse(
                paymentIntent.getClientSecret(),
                paymentIntent.getId(),
                paymentIntent.getAmount(),
                paymentIntent.getCurrency(),
                paymentIntent.getStatus()
            );
            response.setPublishableKey(stripeConfig.getStripePublishableKey());

            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Failed to create PaymentIntent: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve PaymentIntent status by ID
     */
    public PaymentResponse getPaymentStatus(String paymentIntentId) throws StripeException {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            return new PaymentResponse(
                paymentIntent.getClientSecret(),
                paymentIntent.getId(),
                paymentIntent.getAmount(),
                paymentIntent.getCurrency(),
                paymentIntent.getStatus()
            );

        } catch (StripeException e) {
            throw new RuntimeException("Failed to retrieve PaymentIntent: " + e.getMessage(), e);
        }
    }

    /**
     * Confirm a PaymentIntent (for testing purposes)
     */
    public PaymentResponse confirmPaymentIntent(String paymentIntentId) throws StripeException {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent = paymentIntent.confirm();
            
            return new PaymentResponse(
                paymentIntent.getClientSecret(),
                paymentIntent.getId(),
                paymentIntent.getAmount(),
                paymentIntent.getCurrency(),
                paymentIntent.getStatus()
            );

        } catch (StripeException e) {
            throw new RuntimeException("Failed to confirm PaymentIntent: " + e.getMessage(), e);
        }
    }

    /**
     * Get Stripe publishable key for frontend
     */
    public String getPublishableKey() {
        return stripeConfig.getStripePublishableKey();
    }
}
