import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { CheckoutRequest, CheckoutResponse } from '../models/checkout.model';
import { PurchaseSession, PurchaseStats } from '../models/purchase.model';

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
   * Create a Stripe Checkout session
   */
  createCheckoutSession(checkoutRequest: CheckoutRequest): Observable<CheckoutResponse> {
    return this.http.post<CheckoutResponse>(`${this.baseUrl}/create-checkout-session`, checkoutRequest);
  }

  /**
   * Get checkout session details
   */
  getCheckoutSession(sessionId: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/checkout-session/${sessionId}`);
  }

  /**
   * Get all purchase sessions (admin)
   */
  getAllSessions(): Observable<PurchaseSession[]> {
    return this.http.get<PurchaseSession[]>(`${this.baseUrl}/purchases/sessions`);
  }

  /**
   * Get sessions by customer email
   */
  getSessionsByCustomer(email: string): Observable<PurchaseSession[]> {
    return this.http.get<PurchaseSession[]>(`${this.baseUrl}/purchases/sessions/customer/${email}`);
  }

  /**
   * Get sessions by date range (admin)
   */
  getSessionsByDateRange(start: string, end: string): Observable<PurchaseSession[]> {
    return this.http.get<PurchaseSession[]>(`${this.baseUrl}/purchases/sessions/date-range?start=${start}&end=${end}`);
  }
}
