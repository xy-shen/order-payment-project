import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Payment, PaymentStatus } from '../models/payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/payment-api/api/payment';
  private readonly jsonEnumHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  getPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(this.baseUrl);
  }

  getPayment(id: number): Observable<Payment> {
    return this.http.get<Payment>(`${this.baseUrl}/${id}`);
  }

  updatePaymentStatus(id: number, status: PaymentStatus): Observable<Payment> {
    return this.http.patch<Payment>(`${this.baseUrl}/${id}`, JSON.stringify(status), {
      headers: this.jsonEnumHeaders
    });
  }
}
