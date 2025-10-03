import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api';
import { PurchaseSession } from '../../models/purchase.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule,
    MatTooltipModule,
    FormsModule
  ],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss'
})
export class AdminDashboardComponent implements OnInit {
  sessions: PurchaseSession[] = [];
  loading = false;
  displayedColumns: string[] = ['date', 'customer', 'amount', 'status', 'paymentStatus', 'actions'];
  startDate: Date | null = null;
  endDate: Date | null = null;
  totalRevenue: number = 0;
  totalSessions: number = 0;

  constructor(
    private apiService: ApiService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadAllSessions();
  }

  loadAllSessions(): void {
    this.loading = true;
    this.apiService.getAllSessions().subscribe({
      next: (sessions) => {
        this.sessions = sessions;
        this.calculateStats();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading sessions:', error);
        this.snackBar.open('Error loading sessions. Please try again.', 'Close', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
        this.loading = false;
      }
    });
  }

  filterByDateRange(): void {
    if (this.startDate && this.endDate) {
      this.loading = true;
      
      // Format dates as YYYY-MM-DD
      const startStr = this.formatDateForAPI(this.startDate);
      const endStr = this.formatDateForAPI(this.endDate);
      
      this.apiService.getSessionsByDateRange(startStr, endStr).subscribe({
        next: (sessions) => {
          this.sessions = sessions;
          this.calculateStats();
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading sessions by date range:', error);
          this.snackBar.open('Error loading sessions by date range. Please try again.', 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
          });
          this.loading = false;
        }
      });
    } else {
      this.snackBar.open('Please select both start and end dates', 'Close', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top'
      });
    }
  }

  clearFilters(): void {
    this.startDate = null;
    this.endDate = null;
    this.loadAllSessions();
  }

  calculateStats(): void {
    this.totalSessions = this.sessions.length;
    this.totalRevenue = this.sessions
      .filter(session => session.paymentStatus === 'paid')
      .reduce((total, session) => total + session.amountTotal, 0);
  }

  formatDate(timestamp: number): string {
    return new Date(timestamp * 1000).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  formatDateForAPI(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
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
    this.snackBar.open(`Viewing details for session: ${sessionId}`, 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  exportData(): void {
    if (this.sessions.length === 0) return;

    const csv = this.generateCSV();
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `purchase-sessions-${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
    window.URL.revokeObjectURL(url);

    this.snackBar.open('CSV file downloaded successfully!', 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  private generateCSV(): string {
    const headers = ['Date', 'Customer Email', 'Amount', 'Currency', 'Status', 'Payment Status'];
    const rows = this.sessions.map(session => [
      this.formatDate(session.created),
      session.customerEmail,
      (session.amountTotal / 100).toFixed(2),
      session.currency.toUpperCase(),
      session.status,
      session.paymentStatus
    ]);
    
    return [headers, ...rows].map(row => row.join(',')).join('\n');
  }
}
