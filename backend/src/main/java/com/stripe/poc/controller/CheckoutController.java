package com.stripe.poc.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.poc.model.CheckoutRequest;
import com.stripe.poc.model.CheckoutResponse;
import com.stripe.poc.service.StripeCheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for Stripe Checkout operations
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class CheckoutController {
    
    @Autowired
    private StripeCheckoutService checkoutService;
    
    /**
     * Creates a new Stripe Checkout session
     * @param request The checkout request with price ID and customer details
     * @return ResponseEntity with checkout URL or error message
     */
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@Valid @RequestBody CheckoutRequest request) {
        try {
            CheckoutResponse response = checkoutService.createCheckoutSession(request);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create checkout session: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Retrieves checkout session details
     * @param sessionId The session ID to retrieve
     * @return ResponseEntity with session details or error message
     */
    @GetMapping("/checkout-session/{sessionId}")
    public ResponseEntity<?> getCheckoutSession(@PathVariable String sessionId) {
        try {
            Session session = checkoutService.getCheckoutSession(sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", session.getId());
            response.put("status", session.getStatus());
            response.put("paymentStatus", session.getPaymentStatus());
            response.put("customerEmail", session.getCustomerEmail());
            response.put("amountTotal", session.getAmountTotal());
            response.put("currency", session.getCurrency());
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve session: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get all purchase sessions (admin)
     * GET /api/purchases/sessions
     */
    @GetMapping("/purchases/sessions")
    public ResponseEntity<?> getAllSessions() {
        try {
            List<Map<String, Object>> sessions = checkoutService.getAllSessions();
            return ResponseEntity.ok(sessions);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve sessions: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get sessions by customer email
     * GET /api/purchases/sessions/customer/{email}
     */
    @GetMapping("/purchases/sessions/customer/{email}")
    public ResponseEntity<?> getSessionsByCustomer(@PathVariable String email) {
        try {
            List<Map<String, Object>> sessions = checkoutService.getSessionsByCustomer(email);
            return ResponseEntity.ok(sessions);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve customer sessions: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get sessions by date range (admin)
     * GET /api/purchases/sessions/date-range?start=2024-01-01&end=2024-12-31
     */
    @GetMapping("/purchases/sessions/date-range")
    public ResponseEntity<?> getSessionsByDateRange(
            @RequestParam String start,
            @RequestParam String end) {
        try {
            // Convert date strings to timestamps
            LocalDateTime startDate = LocalDateTime.parse(start + "T00:00:00");
            LocalDateTime endDate = LocalDateTime.parse(end + "T23:59:59");
            
            Long startTimestamp = startDate.toEpochSecond(ZoneOffset.UTC);
            Long endTimestamp = endDate.toEpochSecond(ZoneOffset.UTC);
            
            List<Map<String, Object>> sessions = checkoutService.getSessionsByDateRange(startTimestamp, endTimestamp);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve sessions by date range: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
