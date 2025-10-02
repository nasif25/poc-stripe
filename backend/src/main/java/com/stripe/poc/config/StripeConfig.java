package com.stripe.poc.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * Stripe Configuration Class
 * 
 * Configures Stripe SDK and CORS settings for the application
 */
@Configuration
public class StripeConfig implements WebMvcConfigurer {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.publishable-key}")
    private String stripePublishableKey;

    /**
     * Initialize Stripe with secret key
     */
    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = stripeSecretKey;
        System.out.println("✅ Stripe initialized with secret key: " + 
            stripeSecretKey.substring(0, 12) + "...");
    }

    /**
     * Configure CORS for Angular frontend
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:4200", 
                    "http://127.0.0.1:4200"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    public String getStripePublishableKey() {
        return stripePublishableKey;
    }
}
