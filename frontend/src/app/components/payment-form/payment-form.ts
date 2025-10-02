import { Component, OnInit, OnDestroy, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';

import { ApiService } from '../../services/api';
import { StripeService } from '../../services/stripe';
import { Product } from '../../models/product.model';
import { PaymentRequest } from '../../models/payment.model';

@Component({
  selector: 'app-payment-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule
  ],
  templateUrl: './payment-form.html',
  styleUrl: './payment-form.scss'
})
export class PaymentFormComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('cardElement', { static: false }) cardElement!: ElementRef;

  product: Product | null = null;
  paymentForm: FormGroup;
  loading = true;
  processing = false;
  error: string | null = null;
  
  private stripe: any = null;
  private cardElementInstance: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private apiService: ApiService,
    private stripeService: StripeService,
    private snackBar: MatSnackBar
  ) {
    this.paymentForm = this.fb.group({
      customerName: ['', [Validators.required, Validators.minLength(2)]],
      customerEmail: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    this.loadProduct();
    this.initializeStripe();
  }

  ngAfterViewInit(): void {
    // Card element will be initialized after Stripe is loaded
  }

  ngOnDestroy(): void {
    if (this.cardElementInstance) {
      this.cardElementInstance.destroy();
    }
  }

  async loadProduct(): Promise<void> {
    const productId = this.route.snapshot.paramMap.get('productId');
    if (!productId) {
      this.error = 'No product selected';
      this.loading = false;
      return;
    }

    try {
      this.apiService.getProduct(productId).subscribe({
        next: (product) => {
          this.product = product;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading product:', error);
          this.error = 'Failed to load product details';
          this.loading = false;
        }
      });
    } catch (error) {
      console.error('Error loading product:', error);
      this.error = 'Failed to load product details';
      this.loading = false;
    }
  }

  async initializeStripe(): Promise<void> {
    try {
      // Get Stripe config from backend
      this.apiService.getStripeConfig().subscribe({
        next: async (config) => {
          this.stripe = await this.stripeService.initializeStripe(config.publishableKey);
          if (this.stripe && this.cardElement) {
            this.setupCardElement();
          }
        },
        error: (error) => {
          console.error('Error getting Stripe config:', error);
          this.error = 'Failed to initialize payment system';
        }
      });
    } catch (error) {
      console.error('Error initializing Stripe:', error);
      this.error = 'Failed to initialize payment system';
    }
  }

  private setupCardElement(): void {
    if (!this.stripe || !this.cardElement?.nativeElement) return;

    const elements = this.stripeService.createElements();
    if (!elements) return;

    this.cardElementInstance = this.stripeService.createCardElement();
    if (!this.cardElementInstance) return;

    // Mount the card element
    this.cardElementInstance.mount(this.cardElement.nativeElement);

    // Handle real-time validation errors from the card Element
    this.cardElementInstance.on('change', (event: any) => {
      if (event.error) {
        this.error = event.error.message;
      } else {
        this.error = null;
      }
    });
  }

  async onSubmit(): Promise<void> {
    if (!this.paymentForm.valid || !this.product || !this.stripe || !this.cardElementInstance) {
      return;
    }

    this.processing = true;
    this.error = null;

    try {
      const formData = this.paymentForm.value;

      // Create payment request
      const paymentRequest: PaymentRequest = {
        productId: this.product.id,
        amount: this.product.price,
        currency: this.product.currency,
        customerName: formData.customerName,
        customerEmail: formData.customerEmail
      };

      // Create payment intent on backend
      this.apiService.createPaymentIntent(paymentRequest).subscribe({
        next: async (paymentResponse) => {
          try {
            // Confirm payment with Stripe
            const result = await this.stripeService.confirmCardPayment(
              paymentResponse.clientSecret,
              this.cardElementInstance,
              {
                name: formData.customerName,
                email: formData.customerEmail,
              }
            );

            if (result.error) {
              // Payment failed
              this.error = result.error.message || 'Payment failed';
              this.processing = false;
              this.snackBar.open('Payment failed: ' + this.error, 'Close', {
                duration: 5000,
                panelClass: ['error-snackbar']
              });
            } else {
              // Payment succeeded
              this.snackBar.open('Payment successful!', 'Close', {
                duration: 3000,
                panelClass: ['success-snackbar']
              });
              this.router.navigate(['/payment-success'], {
                queryParams: {
                  paymentIntentId: result.paymentIntent.id,
                  product: this.product?.name
                }
              });
            }
          } catch (stripeError) {
            console.error('Stripe confirmation error:', stripeError);
            this.error = 'Payment processing failed';
            this.processing = false;
          }
        },
        error: (backendError) => {
          console.error('Backend error:', backendError);
          this.error = backendError.error?.message || 'Failed to process payment';
          this.processing = false;
          this.snackBar.open('Error: ' + this.error, 'Close', {
            duration: 5000,
            panelClass: ['error-snackbar']
          });
        }
      });

    } catch (error) {
      console.error('Payment error:', error);
      this.error = 'An unexpected error occurred';
      this.processing = false;
    }
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }

  formatPrice(priceInCents: number): string {
    return (priceInCents / 100).toFixed(2);
  }

  // Form getters for validation
  get customerName() { return this.paymentForm.get('customerName'); }
  get customerEmail() { return this.paymentForm.get('customerEmail'); }
}
