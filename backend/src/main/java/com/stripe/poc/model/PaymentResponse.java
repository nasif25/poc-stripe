package com.stripe.poc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payment Response DTO
 */
public class PaymentResponse {
    
    @JsonProperty("clientSecret")
    private String clientSecret;
    
    @JsonProperty("paymentIntentId")
    private String paymentIntentId;
    
    @JsonProperty("amount")
    private Long amount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("publishableKey")
    private String publishableKey;

    // Constructors
    public PaymentResponse() {}

    public PaymentResponse(String clientSecret, String paymentIntentId, 
                          Long amount, String currency, String status) {
        this.clientSecret = clientSecret;
        this.paymentIntentId = paymentIntentId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }

    // Getters and Setters
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPublishableKey() { return publishableKey; }
    public void setPublishableKey(String publishableKey) { this.publishableKey = publishableKey; }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "paymentIntentId='" + paymentIntentId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
