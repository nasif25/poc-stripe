export interface PurchaseSession {
  id: string;
  status: string;
  paymentStatus: string;
  customerEmail: string;
  amountTotal: number;
  currency: string;
  created: number;
  successUrl: string;
  cancelUrl: string;
}

export interface PurchaseStats {
  totalPurchases: number;
  totalRevenue: number;
  averageOrderValue: number;
}

export interface DateRangeFilter {
  start: string;
  end: string;
}
