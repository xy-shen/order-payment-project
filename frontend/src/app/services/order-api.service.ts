import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Order, OrderStatus } from '../models/order.model';

@Injectable({ providedIn: 'root' })
export class OrderApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/order-api/api/orders';
  private readonly jsonEnumHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  createOrder(amount: number): Observable<Order> {
    return this.http.post<Order>(this.baseUrl, { amount });
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.baseUrl);
  }

  getOrder(id: string): Observable<Order> {
    return this.http.get<Order>(`${this.baseUrl}/${id}`);
  }

  updateOrderStatus(id: string, status: OrderStatus): Observable<Order> {
    return this.http.patch<Order>(`${this.baseUrl}/${id}`, JSON.stringify(status), {
      headers: this.jsonEnumHeaders
    });
  }
}
