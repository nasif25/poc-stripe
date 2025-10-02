import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ApiService } from '../../services/api';

@Component({
  selector: 'app-payment-success',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './payment-success.html',
  styleUrl: './payment-success.scss'
})
export class PaymentSuccessComponent implements OnInit {
  sessionId: string | null = null;
  productName: string | null = null;
  loading = true;
  sessionDetails: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.sessionId = params['session_id'] || null;
      if (this.sessionId) {
        this.loadSessionDetails();
      } else {
        this.loading = false;
      }
    });
  }

  loadSessionDetails(): void {
    if (this.sessionId) {
      this.apiService.getCheckoutSession(this.sessionId).subscribe({
        next: (session) => {
          this.sessionDetails = session;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading session details:', error);
          this.loading = false;
        }
      });
    }
  }

  goToProducts(): void {
    this.router.navigate(['/products']);
  }
}
