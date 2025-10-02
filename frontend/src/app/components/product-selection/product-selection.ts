import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ApiService } from '../../services/api';
import { Product } from '../../models/product.model';
import { CheckoutRequest } from '../../models/checkout.model';

@Component({
  selector: 'app-product-selection',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './product-selection.html',
  styleUrl: './product-selection.scss'
})
export class ProductSelectionComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  error: string | null = null;
  processingProducts = new Set<string>();

  constructor(
    private apiService: ApiService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.error = null;
    
    this.apiService.getProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Failed to load products. Please try again.';
        this.loading = false;
      }
    });
  }

  selectProduct(product: Product): void {
    this.processingProducts.add(product.id);
    
    // Create checkout request
    const checkoutRequest: CheckoutRequest = {
      priceId: product.stripePriceId,
      customerEmail: 'customer@example.com', // You can add a simple email input later
      customerName: 'Customer'
    };
    
    this.apiService.createCheckoutSession(checkoutRequest).subscribe({
      next: (response) => {
        if (response.success) {
          // Redirect to Stripe Checkout
          window.location.href = response.checkoutUrl;
        } else {
          this.snackBar.open('Failed to create checkout session: ' + response.message, 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
          });
          this.processingProducts.delete(product.id);
        }
      },
      error: (error) => {
        console.error('Checkout error:', error);
        this.snackBar.open('Error creating checkout session. Please try again.', 'Close', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
        this.processingProducts.delete(product.id);
      }
    });
  }

  formatPrice(priceInCents: number): string {
    return (priceInCents / 100).toFixed(2);
  }

  getPlanIcon(users: number): string {
    switch (users) {
      case 50: return 'group';
      case 100: return 'groups';
      case 200: return 'business';
      case 300: return 'corporate_fare';
      default: return 'group';
    }
  }

  getPlanColor(users: number): string {
    switch (users) {
      case 50: return 'primary';
      case 100: return 'accent';
      case 200: return 'warn';
      case 300: return 'primary';
      default: return 'primary';
    }
  }

  isProcessing(product: Product): boolean {
    return this.processingProducts.has(product.id);
  }

  trackByProductId(index: number, product: Product): string {
    return product.id;
  }
}
