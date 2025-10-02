export interface CheckoutRequest {
  priceId: string;
  customerEmail: string;
  customerName?: string;
}

export interface CheckoutResponse {
  checkoutUrl: string;
  sessionId: string;
  success: boolean;
  message: string;
}
