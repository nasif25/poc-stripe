package com.stripe.poc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Payment Request DTO
 */
public class PaymentRequest {
    
    @NotBlank(message = "Product ID is required")
    @JsonProperty("productId")
    private String productId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @JsonProperty("amount")
    private Long amount;
    
    @NotBlank(message = "Currency is required")
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("customerEmail")
    private String customerEmail;
    
    @JsonProperty("customerName")
    private String customerName;

    // Constructors
    public PaymentRequest() {}

    public PaymentRequest(String productId, Long amount, String currency) {
        this.productId = productId;
        this.amount = amount;
        this.currency = currency;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "productId='" + productId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
