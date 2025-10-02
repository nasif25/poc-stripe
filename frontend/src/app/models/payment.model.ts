export interface PaymentRequest {
  productId: string;
  amount: number;
  currency: string;
  customerEmail?: string;
  customerName?: string;
}

export interface PaymentResponse {
  clientSecret: string;
  paymentIntentId: string;
  amount: number;
  currency: string;
  status: string;
  publishableKey: string;
}

export interface StripeConfig {
  publishableKey: string;
}

