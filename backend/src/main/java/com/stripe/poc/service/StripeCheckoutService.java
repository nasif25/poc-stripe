package com.stripe.poc.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.poc.model.CheckoutRequest;
import com.stripe.poc.model.CheckoutResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for handling Stripe Checkout session creation
 */
@Service
public class StripeCheckoutService {
    
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;
    
    /**
     * Creates a Stripe Checkout session for the given request
     * @param request The checkout request containing price ID and customer info
     * @return CheckoutResponse with checkout URL and session details
     * @throws StripeException if session creation fails
     */
    public CheckoutResponse createCheckoutSession(CheckoutRequest request) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:4200/payment-success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl("http://localhost:4200/payment-cancel")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPrice(request.getPriceId())
                    .build()
            )
            .setCustomerEmail(request.getCustomerEmail())
            .build();

        Session session = Session.create(params);
        
        return new CheckoutResponse(
            session.getUrl(),
            session.getId(),
            true,
            "Checkout session created successfully"
        );
    }
    
    /**
     * Retrieves a checkout session by ID
     * @param sessionId The session ID to retrieve
     * @return Session object
     * @throws StripeException if session retrieval fails
     */
    public Session getCheckoutSession(String sessionId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        return Session.retrieve(sessionId);
    }
}
