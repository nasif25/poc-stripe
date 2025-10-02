package com.stripe.poc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Request model for creating Stripe Checkout sessions
 */
public class CheckoutRequest {
    
    @JsonProperty("priceId")
    @NotBlank(message = "Price ID is required")
    private String priceId;
    
    @JsonProperty("customerEmail")
    @Email(message = "Valid email is required")
    private String customerEmail;
    
    @JsonProperty("customerName")
    private String customerName;

    // Constructors
    public CheckoutRequest() {}

    public CheckoutRequest(String priceId, String customerEmail, String customerName) {
        this.priceId = priceId;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
    }

    // Getters and Setters
    public String getPriceId() { 
        return priceId; 
    }
    
    public void setPriceId(String priceId) { 
        this.priceId = priceId; 
    }

    public String getCustomerEmail() { 
        return customerEmail; 
    }
    
    public void setCustomerEmail(String customerEmail) { 
        this.customerEmail = customerEmail; 
    }

    public String getCustomerName() { 
        return customerName; 
    }
    
    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }

    @Override
    public String toString() {
        return "CheckoutRequest{" +
                "priceId='" + priceId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
