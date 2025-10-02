import { Routes } from '@angular/router';
import { ProductSelectionComponent } from './components/product-selection/product-selection';
import { PaymentFormComponent } from './components/payment-form/payment-form';
import { PaymentSuccessComponent } from './components/payment-success/payment-success';
import { PaymentFailureComponent } from './components/payment-failure/payment-failure';
import { PaymentCancelComponent } from './components/payment-cancel/payment-cancel';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductSelectionComponent },
  { path: 'payment/:productId', component: PaymentFormComponent },
  { path: 'payment-success', component: PaymentSuccessComponent },
  { path: 'payment-failure', component: PaymentFailureComponent },
  { path: 'payment-cancel', component: PaymentCancelComponent },
  { path: '**', redirectTo: '/products' }
];
