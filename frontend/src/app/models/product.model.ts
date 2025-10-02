export interface Product {
  id: string;
  name: string;
  description: string;
  price: number; // Price in cents
  currency: string;
  users: number;
  features: string[];
}

