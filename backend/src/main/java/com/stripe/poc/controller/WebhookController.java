package com.stripe.poc.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Stripe Webhook handling
 * 
 * Handles Stripe webhook events for payment processing
 */
@RestController
@RequestMapping("/api")
public class WebhookController {

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    /**
     * Handle Stripe webhook events
     * POST /api/webhook
     */
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        Event event;

        try {
            // Verify webhook signature
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            // Invalid signature
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid signature");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Webhook error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // Handle the event
        try {
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                    if (paymentIntent != null) {
                        handlePaymentIntentSucceeded(paymentIntent);
                    }
                    break;
                    
                case "payment_intent.payment_failed":
                    PaymentIntent failedPaymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                    if (failedPaymentIntent != null) {
                        handlePaymentIntentFailed(failedPaymentIntent);
                    }
                    break;
                    
                case "payment_intent.created":
                    PaymentIntent createdPaymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                    if (createdPaymentIntent != null) {
                        handlePaymentIntentCreated(createdPaymentIntent);
                    }
                    break;
                    
                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Webhook processing error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Handle successful payment intent
     */
    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        System.out.println("✅ Payment succeeded for PaymentIntent: " + paymentIntent.getId());
        System.out.println("   Amount: $" + (paymentIntent.getAmount() / 100.0));
        System.out.println("   Customer: " + paymentIntent.getMetadata().get("customer_email"));
        System.out.println("   Product: " + paymentIntent.getMetadata().get("product_name"));
        
        // Here you would typically:
        // 1. Update your database with successful payment
        // 2. Send confirmation email to customer
        // 3. Activate the subscription/service
        // 4. Log the transaction
    }

    /**
     * Handle failed payment intent
     */
    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
        System.out.println("❌ Payment failed for PaymentIntent: " + paymentIntent.getId());
        System.out.println("   Amount: $" + (paymentIntent.getAmount() / 100.0));
        System.out.println("   Customer: " + paymentIntent.getMetadata().get("customer_email"));
        System.out.println("   Product: " + paymentIntent.getMetadata().get("product_name"));
        
        // Here you would typically:
        // 1. Update your database with failed payment
        // 2. Send failure notification to customer
        // 3. Log the failed transaction
        // 4. Potentially retry or offer alternative payment methods
    }

    /**
     * Handle created payment intent
     */
    private void handlePaymentIntentCreated(PaymentIntent paymentIntent) {
        System.out.println("🔄 Payment intent created: " + paymentIntent.getId());
        System.out.println("   Amount: $" + (paymentIntent.getAmount() / 100.0));
        System.out.println("   Status: " + paymentIntent.getStatus());
        
        // Here you would typically:
        // 1. Log the payment intent creation
        // 2. Update your database with pending payment
        // 3. Set up any necessary tracking
    }
}

