package com.stripe.poc.controller;

import com.stripe.exception.StripeException;
import com.stripe.poc.model.PaymentRequest;
import com.stripe.poc.model.PaymentResponse;
import com.stripe.poc.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Payment operations
 * 
 * Handles Stripe payment intent creation and status checking
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@Validated
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    /**
     * Create a PaymentIntent for processing payment
     * POST /api/create-payment-intent
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@Valid @RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentResponse paymentResponse = stripeService.createPaymentIntent(paymentRequest);
            return ResponseEntity.ok(paymentResponse);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Payment processing error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An unexpected error occurred");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Get payment status by PaymentIntent ID
     * GET /api/payment-status/{paymentIntentId}
     */
    @GetMapping("/payment-status/{paymentIntentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String paymentIntentId) {
        try {
            PaymentResponse paymentResponse = stripeService.getPaymentStatus(paymentIntentId);
            return ResponseEntity.ok(paymentResponse);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Payment status error");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An unexpected error occurred");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Confirm a PaymentIntent (for testing purposes)
     * POST /api/confirm-payment/{paymentIntentId}
     */
    @PostMapping("/confirm-payment/{paymentIntentId}")
    public ResponseEntity<?> confirmPaymentIntent(@PathVariable String paymentIntentId) {
        try {
            PaymentResponse paymentResponse = stripeService.confirmPaymentIntent(paymentIntentId);
            return ResponseEntity.ok(paymentResponse);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Payment confirmation error");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An unexpected error occurred");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Get Stripe publishable key for frontend
     * GET /api/config
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getStripeConfig() {
        try {
            Map<String, String> config = new HashMap<>();
            config.put("publishableKey", stripeService.getPublishableKey());
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

