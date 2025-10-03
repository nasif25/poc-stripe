import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api';
import { PurchaseSession } from '../../models/purchase.model';

@Component({
  selector: 'app-purchase-history',
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    FormsModule
  ],
  templateUrl: './purchase-history.html',
  styleUrl: './purchase-history.scss'
})
export class PurchaseHistoryComponent implements OnInit {
  sessions: PurchaseSession[] = [];
  customerEmail: string = '';
  loading = false;
  displayedColumns: string[] = ['date', 'amount', 'status', 'paymentStatus', 'actions'];

  constructor(
    private apiService: ApiService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    // Get customer email from localStorage or user input
    this.customerEmail = localStorage.getItem('customerEmail') || '';
    if (this.customerEmail) {
      this.loadPurchaseHistory();
    }
  }

  loadPurchaseHistory(): void {
    if (!this.customerEmail) {
      this.snackBar.open('Please enter your email address', 'Close', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top'
      });
      return;
    }
    
    this.loading = true;
    this.apiService.getSessionsByCustomer(this.customerEmail).subscribe({
      next: (sessions) => {
        this.sessions = sessions;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading purchase history:', error);
        this.snackBar.open('Error loading purchase history. Please try again.', 'Close', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
        this.loading = false;
      }
    });
  }

  formatDate(timestamp: number): string {
    return new Date(timestamp * 1000).toLocaleDateString();
  }

  formatAmount(amount: number, currency: string): string {
    return `$${(amount / 100).toFixed(2)} ${currency.toUpperCase()}`;
  }

  getStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'complete':
        return 'status-complete';
      case 'open':
        return 'status-open';
      case 'expired':
        return 'status-expired';
      default:
        return 'status-default';
    }
  }

  getPaymentStatusClass(paymentStatus: string): string {
    switch (paymentStatus.toLowerCase()) {
      case 'paid':
        return 'payment-paid';
      case 'unpaid':
        return 'payment-unpaid';
      case 'no_payment_required':
        return 'payment-no-required';
      default:
        return 'payment-default';
    }
  }

  viewDetails(sessionId: string): void {
    // Navigate to session details or open modal
    console.log('View details for session:', sessionId);
    this.snackBar.open(`Viewing details for session: ${sessionId}`, 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }
}
