package com.stripe.poc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for Stripe Checkout session creation
 */
public class CheckoutResponse {
    
    @JsonProperty("checkoutUrl")
    private String checkoutUrl;
    
    @JsonProperty("sessionId")
    private String sessionId;
    
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("message")
    private String message;

    // Constructors
    public CheckoutResponse() {}

    public CheckoutResponse(String checkoutUrl, String sessionId, boolean success, String message) {
        this.checkoutUrl = checkoutUrl;
        this.sessionId = sessionId;
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public String getCheckoutUrl() { 
        return checkoutUrl; 
    }
    
    public void setCheckoutUrl(String checkoutUrl) { 
        this.checkoutUrl = checkoutUrl; 
    }

    public String getSessionId() { 
        return sessionId; 
    }
    
    public void setSessionId(String sessionId) { 
        this.sessionId = sessionId; 
    }

    public boolean isSuccess() { 
        return success; 
    }
    
    public void setSuccess(boolean success) { 
        this.success = success; 
    }

    public String getMessage() { 
        return message; 
    }
    
    public void setMessage(String message) { 
        this.message = message; 
    }

    @Override
    public String toString() {
        return "CheckoutResponse{" +
                "checkoutUrl='" + checkoutUrl + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
