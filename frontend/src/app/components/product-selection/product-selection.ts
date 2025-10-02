import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ApiService } from '../../services/api';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-selection',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './product-selection.html',
  styleUrl: './product-selection.scss'
})
export class ProductSelectionComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  error: string | null = null;

  constructor(
    private apiService: ApiService,
    private router: Router
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
    this.router.navigate(['/payment', product.id]);
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

  trackByProductId(index: number, product: Product): string {
    return product.id;
  }
}
