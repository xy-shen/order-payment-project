import { CommonModule, DatePipe, JsonPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Order, OrderStatus } from './models/order.model';
import { Payment, PaymentStatus } from './models/payment.model';
import { OrderApiService } from './services/order-api.service';
import { PaymentApiService } from './services/payment-api.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule, JsonPipe, DatePipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly orderApi = inject(OrderApiService);
  private readonly paymentApi = inject(PaymentApiService);
  private readonly cdr = inject(ChangeDetectorRef);

  protected readonly orderStatuses: OrderStatus[] = ['PENDING_PAYMENT', 'CONFIRMED', 'CANCELLED'];
  protected readonly paymentStatuses: PaymentStatus[] = ['PENDING', 'SUCCESS', 'FAILED', 'CANCELLED'];

  protected createOrderAmount = 100;
  protected orderSearchId = '';
  protected paymentSearchId: number | null = null;
  protected orderStatusUpdateId = '';
  protected selectedOrderStatus: OrderStatus = 'PENDING_PAYMENT';
  protected paymentStatusUpdateId: number | null = null;
  protected selectedPaymentStatus: PaymentStatus = 'PENDING';

  protected readonly orders = signal<Order[]>([]);
  protected readonly payments = signal<Payment[]>([]);
  protected readonly selectedOrder = signal<Order | null>(null);
  protected readonly selectedPayment = signal<Payment | null>(null);
  protected readonly lastResult = signal<unknown>(null);
  protected errorMessage = signal('');
  protected readonly loadingOrders = signal(false);
  protected readonly loadingPayments = signal(false);
  protected readonly ordersFetched = signal(false);
  protected readonly paymentsFetched = signal(false);
  protected readonly ordersCountLabel = computed(() => `${this.orders().length} items`);
  protected readonly paymentsCountLabel = computed(() => `${this.payments().length} items`);

  protected createOrder(): void {
    this.clearError();
    this.orderApi.createOrder(this.createOrderAmount).subscribe({
      next: (order) => {
        this.selectedOrder.set(order);
        this.lastResult.set(order);
        this.orderSearchId = order.id;
        this.fetchOrders();
        this.flushUi();
      },
      error: (error) => {
        this.handleError(error);
        this.flushUi();
      }
    });
  }

  protected fetchOrders(): void {
    this.clearError();
    this.ordersFetched.set(true);
    this.loadingOrders.set(true);
    this.orders.set([]);
    this.orderApi.getOrders().subscribe({
      next: (orders) => {
        this.orders.set([...orders]);
        this.lastResult.set(orders);
        this.loadingOrders.set(false);
        this.flushUi();
      },
      error: (error) => {
        this.loadingOrders.set(false);
        this.handleError(error);
        this.flushUi();
      }
    });
  }

  protected findOrderById(): void {
    if (!this.orderSearchId.trim()) {
      this.errorMessage.set('Enter an order id first.');
      return;
    }

    this.clearError();
    this.orderApi.getOrder(this.orderSearchId.trim()).subscribe({
      next: (order) => {
        this.selectedOrder.set(order);
        this.lastResult.set(order);
        this.flushUi();
      },
      error: (error) => {
        this.handleError(error);
        this.flushUi();
      }
    });
  }

  protected updateOrderStatus(): void {
    if (!this.orderStatusUpdateId.trim()) {
      this.errorMessage.set('Enter an order id to update.');
      return;
    }

    this.clearError();
    this.orderApi.updateOrderStatus(this.orderStatusUpdateId.trim(), this.selectedOrderStatus).subscribe({
      next: (order) => {
        this.selectedOrder.set(order);
        this.lastResult.set(order);
        this.orderSearchId = order.id;
        this.fetchOrders();
        this.fetchPayments();
        this.flushUi();
      },
      error: (error) => {
        this.handleError(error);
        this.flushUi();
      }
    });
  }

  protected fetchPayments(): void {
    this.clearError();
    this.paymentsFetched.set(true);
    this.loadingPayments.set(true);
    this.payments.set([]);
    this.paymentApi.getPayments().subscribe({
      next: (payments) => {
        this.payments.set([...payments]);
        this.lastResult.set(payments);
        this.loadingPayments.set(false);
        this.flushUi();
      },
      error: (error) => {
        this.loadingPayments.set(false);
        this.handleError(error);
        this.flushUi();
      }
    });
  }

  protected findPaymentById(): void {
    if (this.paymentSearchId === null) {
      this.errorMessage.set('Enter a payment id first.');
      return;
    }

    this.clearError();
    this.paymentApi.getPayment(this.paymentSearchId).subscribe({
      next: (payment) => {
        this.selectedPayment.set(payment);
        this.lastResult.set(payment);
        this.flushUi();
      },
      error: (error) => {
        this.handleError(error);
        this.flushUi();
      }
    });
  }

  protected updatePaymentStatus(): void {
    if (this.paymentStatusUpdateId === null) {
      this.errorMessage.set('Enter a payment id to update.');
      return;
    }

    this.clearError();
    this.paymentApi.updatePaymentStatus(this.paymentStatusUpdateId, this.selectedPaymentStatus).subscribe({
      next: (payment) => {
        this.selectedPayment.set(payment);
        this.lastResult.set(payment);
        this.paymentSearchId = payment.id;
        this.fetchPayments();
        this.fetchOrders();
        this.flushUi();
      },
      error: (error) => {
        this.handleError(error);
        this.flushUi();
      }
    });
  }

  protected selectOrder(order: Order): void {
    this.selectedOrder.set(order);
    this.orderSearchId = order.id;
    this.orderStatusUpdateId = order.id;
    this.selectedOrderStatus = order.status;
    this.lastResult.set(order);
  }

  protected selectPayment(payment: Payment): void {
    this.selectedPayment.set(payment);
    this.paymentSearchId = payment.id;
    this.paymentStatusUpdateId = payment.id;
    this.selectedPaymentStatus = payment.status;
    this.lastResult.set(payment);
  }

  private clearError(): void {
    this.errorMessage.set('');
  }

  private handleError(error: unknown): void {
    if (error instanceof HttpErrorResponse) {
      const serverMessage =
        typeof error.error === 'string'
          ? error.error
          : error.error?.message;

      this.errorMessage.set(serverMessage || error.message || 'Request failed.');
      this.lastResult.set(error.error);
      return;
    }

    this.errorMessage.set('Unexpected frontend error.');
  }

  private flushUi(): void {
    this.cdr.detectChanges();
  }
}
