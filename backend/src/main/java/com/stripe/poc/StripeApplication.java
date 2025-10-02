package com.stripe.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Main Spring Boot Application for Stripe Payment Integration POC
 * 
 * This application demonstrates:
 * - Stripe payment integration
 * - RESTful API design
 * - Professional error handling
 * - CORS configuration for Angular frontend
 * 
 * @author Stripe POC Team
 * @version 1.0.0
 */
@SpringBootApplication
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class StripeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StripeApplication.class, args);
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║                 🚀 Stripe POC Backend Started                ║\n" +
            "║                                                              ║\n" +
            "║  📍 Server: http://localhost:8080                           ║\n" +
            "║  📋 API Docs: http://localhost:8080/api                     ║\n" +
            "║  🎯 Health: http://localhost:8080/actuator/health           ║\n" +
            "║                                                              ║\n" +
            "║  💳 Ready for Stripe integration!                           ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n"
        );
    }
}
