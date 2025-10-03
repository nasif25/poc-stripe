package com.stripe.poc.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListParams;
import com.stripe.poc.model.CheckoutRequest;
import com.stripe.poc.model.CheckoutResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

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
    
    /**
     * Get all checkout sessions (purchase history)
     * @return List of session data
     * @throws StripeException if retrieval fails
     */
    public List<Map<String, Object>> getAllSessions() throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        
        SessionListParams params = SessionListParams.builder()
            .setLimit(100L) // Adjust as needed
            .build();
            
        SessionCollection sessions = Session.list(params);
        
        List<Map<String, Object>> sessionList = new ArrayList<>();
        for (Session session : sessions.getData()) {
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("id", session.getId());
            sessionData.put("status", session.getStatus());
            sessionData.put("paymentStatus", session.getPaymentStatus());
            sessionData.put("customerEmail", session.getCustomerEmail());
            sessionData.put("amountTotal", session.getAmountTotal());
            sessionData.put("currency", session.getCurrency());
            sessionData.put("created", session.getCreated());
            sessionData.put("successUrl", session.getSuccessUrl());
            sessionData.put("cancelUrl", session.getCancelUrl());
            sessionList.add(sessionData);
        }
        
        return sessionList;
    }
    
    /**
     * Get sessions by customer email
     * @param customerEmail The customer email to filter by
     * @return List of session data for the customer
     * @throws StripeException if retrieval fails
     */
    public List<Map<String, Object>> getSessionsByCustomer(String customerEmail) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        
        SessionListParams params = SessionListParams.builder()
            .setLimit(100L)
            .build();
            
        SessionCollection sessions = Session.list(params);
        
        List<Map<String, Object>> customerSessions = new ArrayList<>();
        for (Session session : sessions.getData()) {
            if (customerEmail.equals(session.getCustomerEmail())) {
                Map<String, Object> sessionData = new HashMap<>();
                sessionData.put("id", session.getId());
                sessionData.put("status", session.getStatus());
                sessionData.put("paymentStatus", session.getPaymentStatus());
                sessionData.put("customerEmail", session.getCustomerEmail());
                sessionData.put("amountTotal", session.getAmountTotal());
                sessionData.put("currency", session.getCurrency());
                sessionData.put("created", session.getCreated());
                sessionData.put("successUrl", session.getSuccessUrl());
                sessionData.put("cancelUrl", session.getCancelUrl());
                customerSessions.add(sessionData);
            }
        }
        
        return customerSessions;
    }
    
    /**
     * Get sessions by date range
     * @param startDate Start date timestamp
     * @param endDate End date timestamp
     * @return List of session data within date range
     * @throws StripeException if retrieval fails
     */
    public List<Map<String, Object>> getSessionsByDateRange(Long startDate, Long endDate) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        
        SessionListParams params = SessionListParams.builder()
            .setLimit(100L)
            .setCreated(SessionListParams.Created.builder()
                .setGte(startDate)
                .setLte(endDate)
                .build())
            .build();
            
        SessionCollection sessions = Session.list(params);
        
        List<Map<String, Object>> sessionList = new ArrayList<>();
        for (Session session : sessions.getData()) {
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("id", session.getId());
            sessionData.put("status", session.getStatus());
            sessionData.put("paymentStatus", session.getPaymentStatus());
            sessionData.put("customerEmail", session.getCustomerEmail());
            sessionData.put("amountTotal", session.getAmountTotal());
            sessionData.put("currency", session.getCurrency());
            sessionData.put("created", session.getCreated());
            sessionData.put("successUrl", session.getSuccessUrl());
            sessionData.put("cancelUrl", session.getCancelUrl());
            sessionList.add(sessionData);
        }
        
        return sessionList;
    }
}
