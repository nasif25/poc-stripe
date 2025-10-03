import { Routes } from '@angular/router';
import { ProductSelectionComponent } from './components/product-selection/product-selection';
import { PaymentSuccessComponent } from './components/payment-success/payment-success';
import { PaymentFailureComponent } from './components/payment-failure/payment-failure';
import { PaymentCancelComponent } from './components/payment-cancel/payment-cancel';
import { PurchaseHistoryComponent } from './components/purchase-history/purchase-history';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductSelectionComponent },
  { path: 'payment-success', component: PaymentSuccessComponent },
  { path: 'payment-failure', component: PaymentFailureComponent },
  { path: 'payment-cancel', component: PaymentCancelComponent },
  { path: 'purchase-history', component: PurchaseHistoryComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent },
  { path: '**', redirectTo: '/products' }
];
