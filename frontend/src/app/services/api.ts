import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { PaymentRequest, PaymentResponse, StripeConfig } from '../models/payment.model';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  /**
   * Get all available products/pricing tiers
   */
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/products`);
  }

  /**
   * Get specific product by ID
   */
  getProduct(productId: string): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/products/${productId}`);
  }

  /**
   * Create a payment intent
   */
  createPaymentIntent(paymentRequest: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(`${this.baseUrl}/create-payment-intent`, paymentRequest);
  }

  /**
   * Get payment status
   */
  getPaymentStatus(paymentIntentId: string): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(`${this.baseUrl}/payment-status/${paymentIntentId}`);
  }

  /**
   * Get Stripe configuration
   */
  getStripeConfig(): Observable<StripeConfig> {
    return this.http.get<StripeConfig>(`${this.baseUrl}/config`);
  }
}
