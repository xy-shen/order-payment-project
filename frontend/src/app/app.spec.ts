import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { App } from './app';
import { OrderApiService } from './services/order-api.service';
import { PaymentApiService } from './services/payment-api.service';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        {
          provide: OrderApiService,
          useValue: {
            createOrder: () => of({
              id: 'order-1',
              amount: 100,
              status: 'PENDING_PAYMENT',
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            }),
            getOrders: () => of([]),
            getOrder: () => of({
              id: 'order-1',
              amount: 100,
              status: 'PENDING_PAYMENT',
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            }),
            updateOrderStatus: () => of({
              id: 'order-1',
              amount: 100,
              status: 'CONFIRMED',
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            })
          }
        },
        {
          provide: PaymentApiService,
          useValue: {
            getPayments: () => of([]),
            getPayment: () => of({
              id: 1,
              orderId: 'order-1',
              amount: 100,
              eventId: 'event-1',
              status: 'PENDING'
            }),
            updatePaymentStatus: () => of({
              id: 1,
              orderId: 'order-1',
              amount: 100,
              eventId: 'event-1',
              status: 'SUCCESS'
            })
          }
        }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render the api console heading', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Final Project API Console');
  });
});
