import { Injectable } from '@angular/core';
import { loadStripe, Stripe as StripeInstance, StripeElements, StripeCardElement } from '@stripe/stripe-js';

@Injectable({
  providedIn: 'root'
})
export class StripeService {
  private stripePromise: Promise<StripeInstance | null> | null = null;
  private stripe: StripeInstance | null = null;
  private elements: StripeElements | null = null;
  private cardElement: StripeCardElement | null = null;

  constructor() { }

  /**
   * Initialize Stripe with publishable key
   */
  async initializeStripe(publishableKey: string): Promise<StripeInstance | null> {
    if (!this.stripePromise) {
      this.stripePromise = loadStripe(publishableKey);
    }
    this.stripe = await this.stripePromise;
    return this.stripe;
  }

  /**
   * Create Stripe Elements
   */
  createElements(): StripeElements | null {
    if (!this.stripe) {
      console.error('Stripe not initialized');
      return null;
    }
    this.elements = this.stripe.elements();
    return this.elements;
  }

  /**
   * Create card element
   */
  createCardElement(): StripeCardElement | null {
    if (!this.elements) {
      console.error('Stripe Elements not created');
      return null;
    }
    
    this.cardElement = this.elements.create('card', {
      style: {
        base: {
          fontSize: '16px',
          color: '#424770',
          '::placeholder': {
            color: '#aab7c4',
          },
        },
        invalid: {
          color: '#9e2146',
        },
      },
    });
    
    return this.cardElement;
  }

  /**
   * Confirm payment with card element
   */
  async confirmCardPayment(clientSecret: string, cardElement: StripeCardElement, billingDetails?: any) {
    if (!this.stripe) {
      throw new Error('Stripe not initialized');
    }

    return await this.stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: cardElement,
        billing_details: billingDetails,
      }
    });
  }

  /**
   * Get current Stripe instance
   */
  getStripe(): StripeInstance | null {
    return this.stripe;
  }

  /**
   * Get current card element
   */
  getCardElement(): StripeCardElement | null {
    return this.cardElement;
  }
}
