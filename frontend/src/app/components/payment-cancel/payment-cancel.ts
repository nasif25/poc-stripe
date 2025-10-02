import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-payment-cancel',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './payment-cancel.html',
  styleUrl: './payment-cancel.scss'
})
export class PaymentCancelComponent {
  constructor(private router: Router) {}

  goToProducts(): void {
    this.router.navigate(['/products']);
  }

  tryAgain(): void {
    this.router.navigate(['/products']);
  }
}
