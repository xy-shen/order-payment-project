export type OrderStatus = 'PENDING_PAYMENT' | 'CONFIRMED' | 'CANCELLED';

export interface Order {
  id: string;
  amount: number;
  status: OrderStatus;
  createdAt: string;
  updatedAt: string;
}
