export type PaymentStatus = 'PENDING' | 'SUCCESS' | 'FAILED' | 'CANCELLED';

export interface Payment {
  id: number;
  orderId: string;
  amount: number;
  eventId: string;
  status: PaymentStatus;
}
