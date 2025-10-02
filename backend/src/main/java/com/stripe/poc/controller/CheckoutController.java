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
import java.util.HashMap;
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
}
